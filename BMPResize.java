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

        int WidhtAplastado=(int)Math.round(Width * 3 * 0.50);

        byte[][] PixelesThin = new byte[Height][WidhtAplastado];

        byte[] ThinBytes = new byte[WidhtAplastado*Height + 54];


        byte[][] Pixeles = new byte[Height][Width * 3];

        int Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < Width * 3; j++) {
            Pixeles[i][j] = Datos2[Aux];
            Aux++;
          }
        }

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < WidhtAplastado; j+=3) {

            PixelesThin[i][j]=Pixeles[i][j];
            PixelesThin[i][j+1]=Pixeles[i][j+1];
            PixelesThin[i][j+2]=Pixeles[i][j+2];

          }
        }

        // Cambiar datos de cabecera
        
        // Width (bytes 16 17 18 19)
        byte[] widthBy =convertIntToByteArray2(Width/2);
        Datos2[16]=widthBy[3];
        Datos2[17]=widthBy[2];
        Datos2[18]=widthBy[1];
        Datos2[19]=widthBy[0];

        // Heigth (bytes 25, 26, 27, 28)
        byte[] heigthBy =convertIntToByteArray2(Height);
        Datos2[22]=heigthBy[3];
        Datos2[23]=heigthBy[2];
        Datos2[24]=heigthBy[1];
        Datos2[25]=heigthBy[0];
        

        // height en la 22? wifht 16


        
        for (int i = 0; i < 54; i++) {
          ThinBytes[i]=Datos2[i];
        }

        Aux = 54;

        for (int i = 0; i < Height; i++) {
          for (int j = 0; j < WidhtAplastado; j++) {
            ThinBytes[Aux] = PixelesThin[i][j];
            Aux++;
          }
        }

        FileOutputStream Imagen = new FileOutputStream(Nombre.substring(0, Nombre.indexOf(".")) + "Thin.bmp");
        Imagen.write(ThinBytes);
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
}