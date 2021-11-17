import java.io.*;
import java.util.regex.Pattern;

//javac *.java && clear && java BMPImageHandler -grayscale Muestra2.bmp

public class BMPGrayscale {
  public void Grayscale(String Nombre) throws Exception {
    // Se repite el codigo de Grayscale
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
        byte[] Datos2Gray = new byte[256 * 4 + Width * Height + 54];
        Archivo.read(Datos2);
        Archivo.close();


        // Matriz de pixeles
        byte[][] Pixeles = new byte[Height][Width * 3];
        int Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < Width * 3; j++) {
            Pixeles[i][j] = Datos2[Aux];
            Aux++;
          }
        }

        byte[] PixelesGray = new byte[Width*Height];

        Aux = 0;
        for (int j = 0; j < Height; j ++) {
          for (int i = 0; i < Width * 3; i += 3) {
            int R = Pixeles[j][i + 2] & 0xff;
            int G = Pixeles[j][i + 1] & 0xff;
            int B = Pixeles[j][i] & 0xff;

            int Tgb = (int) Math.round(0.299 * R + 0.587 * G + 0.114 * B);
            byte gb;

            if (Tgb > 255) {
              gb = (byte)255;
            }
            else {
              gb = (byte)Tgb; 
            }

            PixelesGray[Aux] = gb;
            Aux++;
          }
        }

        // Copiar datos comunes de la cabecera
        for (int i = 0; i < 54; i++) {
          Datos2Gray[i] = Datos2[i];
        }

        // Cambiar datos de cabecera

        // File size (bytes 2,3,4,5)
        byte[] fileSBy = convertIntToByteArray2(54 + 256 * 4+ Width * Height);
        Datos2Gray[2] = fileSBy[3];
        Datos2Gray[3] = fileSBy[2];
        Datos2Gray[4] = fileSBy[1];
        Datos2Gray[5] = fileSBy[0];

        // DataOffset (bytes 10, 11, 12, 13)
        byte[] dataOfBy = convertIntToByteArray2(54 + 256 * 4);
        Datos2Gray[10] = dataOfBy[3];
        Datos2Gray[11] = dataOfBy[2];
        Datos2Gray[12] = dataOfBy[1];
        Datos2Gray[13] = dataOfBy[0];

        // BitCount (bytes 28, 29)
        byte[] dataCountBy = convertIntToByteArray1(8);
        Datos2Gray[28] = dataCountBy[1];
        Datos2Gray[29] = dataCountBy[0];

        // Image Size (34, 35, 36, 37)
        byte[] imageSBy = convertIntToByteArray2(Width * Height);
        Datos2Gray[34] = imageSBy[3];
        Datos2Gray[35] = imageSBy[2];
        Datos2Gray[36] = imageSBy[1];
        Datos2Gray[37] = imageSBy[0];

        // Colors used (46, 47, 48, 49)
        byte[] colorsUsBy = convertIntToByteArray2(256);
        Datos2Gray[46] = colorsUsBy[3];
        Datos2Gray[47] = colorsUsBy[2];
        Datos2Gray[48] = colorsUsBy[1];
        Datos2Gray[49] = colorsUsBy[0];

        // Colors important (46, 47, 48, 49)
        byte[] colorsImpBy = convertIntToByteArray2(0);
        Datos2Gray[50] = colorsImpBy[3];
        Datos2Gray[51] = colorsImpBy[2];
        Datos2Gray[52] = colorsImpBy[1];
        Datos2Gray[53] = colorsImpBy[0];

        // Color table
        Aux = 54;
        for (int i = 0; i < 256; i++) {
          byte color = (byte)i;
          byte reserved = (byte)0;

          Datos2Gray[Aux] = color;
          Datos2Gray[Aux+1] = color;
          Datos2Gray[Aux+2] = color;
          Datos2Gray[Aux+3] = reserved;
          Aux += 4;
        }

        // Raster data
        Aux = 54 + 256 * 4;
        for (int i = 0; i < Width * Height; i++) {
            Datos2Gray[Aux] = PixelesGray[i];
            Aux++;
        }
        

        FileOutputStream Imagen = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "Grayscale.bmp");
        Imagen.write(Datos2Gray);
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

  byte[] convertIntToByteArray2(int value) {
    return new byte[] {
      (byte)(value >> 24),
      (byte)(value >> 16),
      (byte)(value >> 8),
      (byte)value };
  }

  byte[] convertIntToByteArray1(int value) {
    return new byte[] {
      (byte)(value >> 8),
      (byte)value };
  }
}