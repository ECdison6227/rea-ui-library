import jadx.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Supplement REA's method Evidence with navigable decompiler output.
class ExportJava {
  public static void main(String[] argv) throws Exception {
    JadxArgs opts = new JadxArgs();
    opts.setInputFiles(List.of(new File(argv[0])));
    opts.setThreadsCount(4);
    opts.setTypeUpdatesLimitCount(100000);
    opts.setCommentsLevel(jadx.api.CommentsLevel.DEBUG);
    try (JadxDecompiler jadx = new JadxDecompiler(opts)) {
      jadx.load();
      Path root = Path.of(argv[1]);
      Files.createDirectories(root);
      List<String> inventory = new ArrayList<>();
      int count=0;
      for (JavaClass cls : jadx.getClasses()) {
        String name=cls.getFullName();
        inventory.add(name);
        if (argv.length > 2 ? name.equals(argv[2]) : name.startsWith("ai.mirofish.") || name.startsWith("com.capacitorjs.plugins.localnotifications.") || name.startsWith("com.aparajita.capacitor.securestorage.")) {
          Path out=root.resolve(name.replace('.','/')+".java");
          Files.createDirectories(out.getParent());
          try {Files.writeString(out,cls.getCode()); count++;}
          catch(Exception e) {System.err.println(name+": "+e);}
        }
      }
      Files.write(root.resolve("class-inventory.txt"),inventory);
      for (ResourceFile res:jadx.getResources()) {
        if (res.getOriginalName().equals("AndroidManifest.xml")) {
          Files.writeString(root.resolve("AndroidManifest.xml"),res.loadContent().getText().getCodeStr());
        }
      }
      System.out.println("Exported "+count+" classes, inventoried "+inventory.size());
    }
  }
}
