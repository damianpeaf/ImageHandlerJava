import java.io.*;
import java.util.regex.Pattern;

public class BMPGrayscale {
  public void Grayscale(String Nombre) throws Exception {
    try {
      if (Pattern.matches("\\w+.bmp", Nombre)) {
        FileInputStream Archivo = new FileInputStream(Nombre);
        byte[] Datos = new byte[54];
        Archivo.read(Datos);
        Archivo.close();

        int Width = ((Datos[21] & 0xFF) << 24) | ((Datos[20] & 0xFF) << 16) | ((Datos[19] & 0xFF) << 8) | (Datos[18] & 0xFF);
        int Height = ((Datos[25] & 0xFF) << 24) | ((Datos[24] & 0xFF) << 16) | ((Datos[23] & 0xFF) << 8) | (Datos[22] & 0xFF);

        Archivo = new FileInputStream(Nombre);
        byte[] Datos2 = new byte[Width * 3 * Height + 54];
        Archivo.read(Datos2);
        Archivo.close();

        byte[][] Pixeles = new byte[Width * 3][Height];
        int Aux = 54;

        for (int i = 0; i < Width * 3; i++) {
          for (int j = 0; j < Height; j++) {
            Pixeles[i][j] = Datos2[Aux];
            Aux++;
          }
        }

        for (int i = 0; i < Width * 3; i++) {
          for (int j = 0; j < Height; j += 3) {
            int R = Pixeles[i][j + 2] & 0xff;
            int G = Pixeles[i][j + 1] & 0xff;
            int B = Pixeles[i][j] & 0xff;

            int Tgb = (int) Math.round(0.299 * R + 0.587 * G + 0.114 * B);
            byte gb;

            if (Tgb > 255) {
              gb = (byte)255;
            }
            else {
              gb = (byte)Tgb; 
            }

            Pixeles[i][j + 2] = gb;
            Pixeles[i][j + 1] = gb;
            Pixeles[i][j] = gb;
          }
        }

        Aux = 54;

        for (int i = 0; i < Width * 3; i++) {
          for (int j = 0; j < Height; j++) {
            Datos2[Aux] = Pixeles[i][j];
            Aux++;
          }
        }
        
        FileOutputStream Imagen = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "Grayscale.bmp");
        Imagen.write(Datos2);
        Imagen.close();
      }
      else {
        System.out.println("¡Tipo de archivo ingresado incorrecto!");
      }
    }
    catch (FileNotFoundException Error) {
      System.out.println("*No se ha encontrado el archivo*");
    }
  }
}