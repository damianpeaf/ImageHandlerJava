import java.io.*;
import java.util.regex.Pattern;

//javac *.java && clear && java BMPImageHandler -rotate Muestra2.bmp

public class BMPRotations {
  public void Rotations(String Nombre) throws Exception {
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

        byte[][] Pixeles = new byte[Height][Width * 3];
        int Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < Width * 3; j++) {
            Pixeles[i][j] = Datos2[Aux];
            Aux++;
          }
        }

        // Rotacion horizontal
        byte[][] PixelesAux = new byte[Height][Width * 3];
        int indiceWidth = (Width * 3) - 1;
        int indiceHeight = 0;

        for (int i = Height - 1; i >= 0 ; i--) {
          indiceWidth = (Width * 3) - 1;

          for (int j = indiceWidth; j >= 0; j--) {
            PixelesAux[indiceHeight][indiceWidth] = Pixeles[i][j];
            indiceWidth--;
          }

          indiceHeight++;
        }

        Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < Width * 3; j++) {
            Datos2[Aux] = PixelesAux[i][j];
            Aux++;
          }
        }

        FileOutputStream Imagen = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "HRotation.bmp");
        Imagen.write(Datos2);
        Imagen.close();

        // Rotacion vertical
        indiceWidth = 0;
        indiceHeight = Height-1;

        for (int i = Height - 1; i >= 0 ; i--) {
          indiceWidth = 0;

          for (int j = (Width*3)-1; j >= 0; j--) {
            PixelesAux[indiceHeight][indiceWidth] = Pixeles[i][j];
            indiceWidth++;
          }

          indiceHeight--;
        }

        Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < Width * 3; j += 3) {
            Datos2[Aux] = PixelesAux[i][j + 2];
            Datos2[Aux + 1] = PixelesAux[i][j + 1];
            Datos2[Aux + 2] = PixelesAux[i][j];
            Aux += 3;
          }
        }

        FileOutputStream Imagen2 = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "VRotation.bmp");
        Imagen2.write(Datos2);
        Imagen2.close();
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