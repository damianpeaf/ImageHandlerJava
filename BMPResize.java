import java.io.*;
import java.util.regex.Pattern;

//javac *.java && clear && java BMPImageHandler -resize Muestra2.bmp

public class BMPResize {
  public void Resize(String Nombre) throws Exception {
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

        int WidhtAplastado = (int)Math.round(Width * 3 /2);
        byte[][] pixelesThin = new byte[Height][WidhtAplastado];
        byte[] thinBytes = new byte[WidhtAplastado * Height + 54];

        // Matriz de pixeles
        byte[][] Pixeles = new byte[Height][Width * 3];
        int Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < Width * 3; j++) {
            Pixeles[i][j] = Datos2[Aux];
            Aux++;
          }
        }

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < WidhtAplastado; j += 3) {
            pixelesThin[i][j] = Pixeles[i][2 * j];
            pixelesThin[i][j + 1] = Pixeles[i][2 * j + 1];
            pixelesThin[i][j + 2] = Pixeles[i][2 * j + 2];
          }
        }

        // Cambiar datos de cabecera
        // File size (bytes 2,3,4,5)
        byte[] fileSBy = convertIntToByteArray2(WidhtAplastado * Height + 54);
        Datos2[2] = fileSBy[3];
        Datos2[3] = fileSBy[2];
        Datos2[4] = fileSBy[1];
        Datos2[5] = fileSBy[0];
        
        // Width (bytes 16 17 18 19)
        byte[] widthBy = convertIntToByteArray2(Width / 2);
        Datos2[18] = widthBy[3];
        Datos2[19] = widthBy[2];
        Datos2[20] = widthBy[1];
        Datos2[21] = widthBy[0];

        // Heigth (bytes 22)
        byte[] heigthBy = convertIntToByteArray2(Height);
        Datos2[22] = heigthBy[3];
        Datos2[23] = heigthBy[2];
        Datos2[24] = heigthBy[1];
        Datos2[25] = heigthBy[0];
        
        for (int i = 0; i < 54; i++) {
          thinBytes[i] = Datos2[i];
        }

        Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < WidhtAplastado; j++) {
            thinBytes[Aux] = pixelesThin[i][j];
            Aux++;
          }
        }

        FileOutputStream Imagen = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "Thin.bmp");
        Imagen.write(thinBytes);
        Imagen.close(); 

        Archivo = new FileInputStream(Nombre);
        byte[] Datos3 = new byte[Width * 3 * Height + 54];
        Archivo.read(Datos3);
        Archivo.close();

        int heightComprimido = (int)Math.round(Height / 2);
        byte[][] pixelesFlat = new byte[heightComprimido][Width * 3];
        byte[] FlatBytes = new byte[heightComprimido * Width * 3 + 54];

        for (int i = 0; i < heightComprimido; i++) {
          for (int j = 0; j < Width * 3; j += 3) {
            pixelesFlat[i][j] = Pixeles[2 * i][j];
            pixelesFlat[i][j + 1] = Pixeles[2 * i][j + 1];
            pixelesFlat[i][j + 2] = Pixeles[2 * i][j + 2];
          }
        }
        
        for (int i = 0; i < 54; i++) {
          FlatBytes[i] = Datos3[i];
        }

        Aux = 54;

        for (int i = 0; i < heightComprimido; i++) {
          for (int j = 0; j < Width * 3; j++) {
            FlatBytes[Aux] = pixelesFlat[i][j];
            Aux++;
          }
        }

        FileOutputStream Imagen2 = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "Flat.bmp");
        Imagen2.write(FlatBytes);
        Imagen2.close();

      }
      else {
        System.out.println("??Tipo de archivo ingresado incorrecto!");
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
}