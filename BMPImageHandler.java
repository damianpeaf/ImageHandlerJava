public class BMPImageHandler {
  public static void main(String[] args) throws Exception {
    if (args.length == 1) {
      if (args[0].equals("-help")) {
        System.out.println("help");
      }
      else {
        System.out.println("Comando incorrecto!");
      }
    } 
    else if(args.length == 2) {
      if (args[0].equals("-core")) {
        BMPCore Si = new BMPCore();
        Si.Core(args[1]);
      } 
      else if(args[0].equals("-rotate")) {
        BMPRotations Si = new BMPRotations();
        Si.Rotations(args[1]);
      }
      else if(args[0].equals("-resize")) {
        BMPResize Si = new BMPResize();
        Si.Resize(args[1]);
      }
      else if(args[0].equals("-grayscale")) {
        BMPGrayscale Si = new BMPGrayscale();
        Si.Grayscale(args[1]);
      }
      else if(args[0].equals("-rle")) {
        System.out.println("rle");
      }
      else {
        System.out.println("Comando incorrecto!");
      }
    }
    else if(args.length == 3) {
      if(args[0].equals("-kernel")) {
        System.out.println("kernel");
      }
      else if(args[0].equals("-all")) {
        System.out.println("all");
      } 
      else {
        System.out.println("Comando incorrecto!");
      }
    }
    else {
      System.out.println("Comando incorrecto!");
    }
  }
}