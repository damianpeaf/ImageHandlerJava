import java.io.*;
import java.util.regex.Pattern;

//javac *.java && clear && java BMPImageHandler -rotate Imagen.bmp

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

        for (int j = 0; j < Height; j++) {
          for (int i = 0; i < Width * 3; i++) {
            Pixeles[j][i] = Datos2[Aux];
            Aux++;
          }
        }

        byte[][] PixelesAux = new byte[Height][Width * 3];
        int indiceWidth = (Width * 3) -1;
        int indiceHeight = 0;


        for (int j = (Height) - 1; j >= 0 ; j--) {
          indiceWidth = (Width * 3) -1;
          for (int i = (Width * 3)-1; i >=0; i--) {
            PixelesAux[indiceHeight][indiceWidth] = Pixeles[j][i];
            indiceWidth--;
          }
          indiceHeight++;
        }

        Aux = 54;

          for (int j = 0; j < Height; j++) {
            for (int i = 0; i < Width * 3; i++) {
              Datos2[Aux] = PixelesAux[j][i];
              Aux++;
            }
          }

          FileOutputStream Imagen = new FileOutputStream("volteadoHorizontal.bmp");
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