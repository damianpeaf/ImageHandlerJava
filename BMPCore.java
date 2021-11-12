import java.io.*;
import java.util.regex.Pattern;

public class BMPCore {
  public void Core(String Nombre) throws Exception {
    try {
      if (Pattern.matches("\\w+.bmp", Nombre)) {
        FileInputStream Archivo = new FileInputStream(Nombre);
        byte[] Datos = new byte[54];
        Archivo.read(Datos);
        Archivo.close();

        int Width = ((Datos[21] & 0xFF) << 24) | ((Datos[20] & 0xFF) << 16) | ((Datos[19] & 0xFF) << 8) | (Datos[18] & 0xFF);
        int Height = ((Datos[25] & 0xFF) << 24) | ((Datos[24] & 0xFF) << 16) | ((Datos[23] & 0xFF) << 8) | (Datos[22] & 0xFF);

        for (int Color = 0; Color < 4; Color++) {
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
              // Rojo 
              if (Color == 0) {
                Pixeles[i][j] = 0;
                Pixeles[i][j + 1] = 0;
              }
              // Verde
              else if (Color == 1) {
                Pixeles[i][j] = 0;
                Pixeles[i][j + 2] = 0;
              }
              // Azul
              else if (Color == 2) {
                Pixeles[i][j + 1] = 0;
                Pixeles[i][j + 2] = 0;
              }
              // Sepia
              else {
                int R = Pixeles[i][j + 2] & 0xff;
                int G = Pixeles[i][j + 1] & 0xff;
                int B = Pixeles[i][j] & 0xff;

                int tr = (int) Math.round(0.393 * R + 0.769 * G + 0.189 * B);
                int tg = (int) Math.round(0.349 * R + 0.686 * G + 0.168 * B);
                int tb = (int) Math.round(0.272 * R + 0.534 * G + 0.131 * B);

                byte r, g, b;

                if (tr > 255) {
                  r = (byte)255;
                }
                else {
                  r = (byte)tr; 
                }

                if (tg > 255) {
                  g = (byte)255;
                }
                else {
                  g = (byte)tg; 
                }

                if (tb > 255) {
                  b = (byte)255;
                }
                else {
                  b = (byte)tb; 
                }

                Pixeles[i][j + 2] = r;
                Pixeles[i][j + 1] = g;
                Pixeles[i][j] = b;
              }
            }
          }

          Aux = 54;

          for (int i = 0; i < Width * 3; i++) {
            for (int j = 0; j < Height; j++) {
              Datos2[Aux] = Pixeles[i][j];
              Aux++;
            }
          }

          String[] Colores = {"Red", "Green", "Blue", "Sepia"};
        
          FileOutputStream Imagen = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + Colores[Color] + ".bmp");
          Imagen.write(Datos2);
          Imagen.close();
        }   
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