import jadx.api.*;
import jadx.core.xmlgen.ResContainer;
import java.io.*;
import java.nio.file.*;
import java.util.*;
class ExportResources {
  static Path root;
  static void write(ResContainer content) throws Exception {
    if(content.getText()!=null){
      Path out=root.resolve(content.getName()).normalize();
      if(out.startsWith(root)){
        Files.createDirectories(out.getParent());
        Files.writeString(out,content.getText().getCodeStr());
      }
    }
    for(ResContainer child:content.getSubFiles())write(child);
  }
  public static void main(String[] args) throws Exception {
    root=Path.of(args[1]).toAbsolutePath(); Files.createDirectories(root);
    JadxArgs opts=new JadxArgs(); opts.setInputFiles(List.of(new File(args[0])));
    try(JadxDecompiler jadx=new JadxDecompiler(opts)){
      jadx.load();
      List<String> manifest=new ArrayList<>();
      for(ResourceFile resource:jadx.getResources()){
        manifest.add(resource.getOriginalName()+"\t"+resource.getDeobfName()+"\t"+resource.getType());
        if(resource.getType()==ResourceType.ARSC || resource.getType()==ResourceType.XML || resource.getType()==ResourceType.MANIFEST){
          try{write(resource.loadContent());}catch(Exception e){System.err.println(resource+": "+e);}
        }
      }
      Files.write(root.resolve("resource-index.tsv"),manifest);
      System.out.println("Decoded resources to "+root);
    }
  }
}
