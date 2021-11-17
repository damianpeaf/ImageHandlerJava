public class BMPImageHandler {
  public static void main(String[] args) throws Exception {
    if (args.length == 1) {
      if (args[0].equals("-help")) {
        System.out.println("java BMPImageHandler -core imagen.bmp");
        System.out.println("java BMPImageHandler -rotate imagen.bmp");
        System.out.println("java BMPImageHandler -resize imagen.bmp");
        System.out.println("java BMPImageHandler -grayscale imagen.bmp");
        System.out.println("java BMPImageHandler -rle imagen.bmp");
        System.out.println("java BMPImageHandler -kernel kernel.txt imagen.bmp");
        System.out.println("java BMPImageHandler -all kernel.txt imagen.bmp");
      }
      else {
        System.out.println("Comando incorrecto!");
      }
    } 
    else if(args.length == 2) {
      if (args[0].equals("-core")) {
        BMPCore Imagen = new BMPCore();
        Imagen.Core(args[1]);
      } 
      else if(args[0].equals("-rotate")) {
        BMPRotations Imagen = new BMPRotations();
        Imagen.Rotations(args[1]);
      }
      else if(args[0].equals("-resize")) {
        BMPResize Imagen = new BMPResize();
        Imagen.Resize(args[1]);
      }
      else if(args[0].equals("-grayscale")) {
        BMPGrayscale Imagen = new BMPGrayscale();
        Imagen.Grayscale(args[1]);
      }
      else if(args[0].equals("-rle")) {
        BMPRunLengthEncoding Imagen = new BMPRunLengthEncoding();
        Imagen.RunLengthEncoding(args[1]);
      }
      else {
        System.out.println("¡Comando incorrecto!");
      }
    }
    else if(args.length == 3) {
      if(args[0].equals("-kernel")) {
        BMPRunKernelFilter Imagen = new BMPRunKernelFilter();
        Imagen.RunKernelFilter(args[1], args[2]);
      }
      else if(args[0].equals("-all")) {
        BMPCore imagenCore = new BMPCore();
        imagenCore.Core(args[2]);
        BMPRotations imagenRotations = new BMPRotations();
        imagenRotations.Rotations(args[2]);
        BMPResize imagenResize = new BMPResize();
        imagenResize.Resize(args[2]);
        BMPGrayscale imagenGrayScale = new BMPGrayscale();
        imagenGrayScale.Grayscale(args[2]);
        BMPRunLengthEncoding imagenEncoding = new BMPRunLengthEncoding();
        imagenEncoding.RunLengthEncoding(args[2]);
        BMPRunKernelFilter imagenKernel = new BMPRunKernelFilter();
        imagenKernel.RunKernelFilter(args[1], args[2]);
      } 
      else {
        System.out.println("¡Comando incorrecto!");
      }
    }
    else {
      System.out.println("Comando incorrecto!");
    }
  }
}