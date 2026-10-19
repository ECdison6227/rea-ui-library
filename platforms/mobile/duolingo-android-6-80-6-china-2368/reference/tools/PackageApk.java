import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import java.util.zip.*;

/** Build-only host tool: writes canonical member names and explicitly STORED font/assets. */
public final class PackageApk {
    private static byte[] bytes(InputStream in) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();byte[] buffer=new byte[8192];int n;
        while((n=in.read(buffer))!=-1)out.write(buffer,0,n);return out.toByteArray();
    }
    private static String sha(byte[] data)throws Exception {
        byte[] digest=MessageDigest.getInstance("SHA-256").digest(data);StringBuilder b=new StringBuilder();
        for(byte v:digest)b.append(String.format(Locale.ROOT,"%02x",v&255));return b.toString();
    }
    private static List<Path> assets(Path root)throws IOException {
        List<Path> files=new ArrayList<>();try(java.util.stream.Stream<Path> stream=Files.walk(root)){stream.filter(Files::isRegularFile).forEach(files::add);}Collections.sort(files);return files;
    }
    private static String name(Path root,Path file){return "assets/"+root.relativize(file).toString().replace('\\','/');}
    private static void put(ZipOutputStream out,String name,byte[] data,int method,long time)throws IOException {
        if(name.indexOf('\\')>=0)throw new IOException("Noncanonical member "+name);
        ZipEntry entry=new ZipEntry(name);entry.setMethod(method);if(time>=0)entry.setTime(time);
        if(method==ZipEntry.STORED){CRC32 crc=new CRC32();crc.update(data);entry.setSize(data.length);entry.setCompressedSize(data.length);entry.setCrc(crc.getValue());}
        out.putNextEntry(entry);out.write(data);out.closeEntry();
    }
    public static void main(String[] args)throws Exception {
        if(args.length==3&&args[0].equals("--verify")){
            Path assets=Paths.get(args[2]);
            try(ZipFile zip=new ZipFile(args[1])){
                Set<String> names=new HashSet<>();Enumeration<? extends ZipEntry> members=zip.entries();
                while(members.hasMoreElements()){String name=members.nextElement().getName();if(name.indexOf('\\')>=0||!names.add(name))throw new IOException("Invalid/duplicate APK member "+name);}
                for(Path file:assets(assets)){
                    String name=name(assets,file);ZipEntry entry=zip.getEntry(name);if(entry==null)throw new IOException("Missing "+name);
                    if(entry.getMethod()!=ZipEntry.STORED)throw new IOException("Asset is not STORED: "+name+" method="+entry.getMethod());
                    byte[] actual;try(InputStream in=zip.getInputStream(entry)){actual=bytes(in);}
                    String actualSha=sha(actual);if(!actualSha.equals(sha(Files.readAllBytes(file))))throw new IOException("SHA mismatch "+name);
                    System.out.println("PASS "+name+" method=STORED(0) bytes="+actual.length+" sha256="+actualSha);
                }
                ZipEntry dex=zip.getEntry("classes.dex");if(dex==null||dex.getSize()==0)throw new IOException("Missing nonempty classes.dex");
                byte[] actual;try(InputStream in=zip.getInputStream(dex)){actual=bytes(in);}System.out.println("PASS classes.dex bytes="+actual.length+" sha256="+sha(actual));
            }
            return;
        }
        if(args.length!=4)throw new IllegalArgumentException("PackageApk <resources.ap_> <unsigned.apk> <assets directory> <dex directory> OR --verify <signed.apk> <assets directory>");
        Path assets=Paths.get(args[2]),dex=Paths.get(args[3]);
        try(ZipOutputStream out=new ZipOutputStream(Files.newOutputStream(Paths.get(args[1])));ZipFile resources=new ZipFile(args[0])){
            Enumeration<? extends ZipEntry> entries=resources.entries();while(entries.hasMoreElements()){
                ZipEntry entry=entries.nextElement();if(entry.isDirectory())continue;byte[] data;try(InputStream in=resources.getInputStream(entry)){data=bytes(in);}put(out,entry.getName(),data,entry.getMethod(),entry.getTime());
            }
            for(Path file:assets(assets))put(out,name(assets,file),Files.readAllBytes(file),ZipEntry.STORED,Files.getLastModifiedTime(file).toMillis());
            try(java.util.stream.Stream<Path> stream=Files.list(dex)){for(Path file:(Iterable<Path>)stream.filter(p->p.toString().endsWith(".dex")).sorted()::iterator)put(out,file.getFileName().toString(),Files.readAllBytes(file),ZipEntry.DEFLATED,Files.getLastModifiedTime(file).toMillis());}
        }
    }
}
