import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class BMPRunKernelFilter {
  public void RunKernelFilter(String NombreK, String NombreI) throws Exception {
    try {
      if (Pattern.matches("\\w+.bmp", NombreI)&&(Pattern.matches("\\w+.txt", NombreK))) {
        FileInputStream Archivo = new FileInputStream(NombreI);
        byte[] Datos = new byte[54];
        Archivo.read(Datos);
        Archivo.close();

        //Datos del kernel
        File f = new File(NombreK);
        float[] kernel = new float[9];
        Scanner sc = new Scanner(f);
        int c = 0;

        while(sc.hasNextFloat()){
          kernel[c] = sc.nextFloat();
          c++;
        }

        int Width = ((Datos[21] & 0xFF) << 24) | ((Datos[20] & 0xFF) << 16) | ((Datos[19] & 0xFF) << 8) | (Datos[18] & 0xFF);
        int Height = ((Datos[25] & 0xFF) << 24) | ((Datos[24] & 0xFF) << 16) | ((Datos[23] & 0xFF) << 8) | (Datos[22] & 0xFF);

        Archivo = new FileInputStream(NombreI);
        byte[] Datos2 = new byte[Width * 3 * Height + 54];
        Archivo.read(Datos2);
        Archivo.close();
        //Matrices por color sin kernel
        byte[][] PixelesR = new byte[Height][Width];
        byte[][] PixelesG = new byte[Height][Width];
        byte[][] PixelesB = new byte[Height][Width];
        //Matrices por color con kernel
        byte[][] PixelRAux = new byte[Height][Width];
        byte[][] PixelGAux = new byte[Height][Width];
        byte[][] PixelBAux = new byte[Height][Width];
        int Aux = 54;

        for (int j = 0; j < Height; j++) {
          for (int i = 0; i < Width; i++) {
            PixelesB[j][i] = Datos2[Aux];
            Aux += 3;
          }
        }

        Aux = 55;

        for (int j = 0; j < Height; j++) {
          for (int i = 0; i < Width; i++) {
            PixelesG[j][i] = Datos2[Aux];
            Aux += 3;
          }
        }

        Aux = 56;

        for (int j = 0; j < Height; j++) {
          for (int i = 0; i < Width; i++) {
            PixelesR[j][i] = Datos2[Aux];
            Aux += 3;
          }
        }
        
        //Kernel a Matriz Azul
        for(int i = 0; i < Height; i++) {
          for(int j = 0; j < Width; j++) {
            float cont = 0;
            
            if ((i == 0) && (j == 0)) {
              cont = (float)PixelesB[i][j] * (float)kernel[4] +(float)PixelesB[i][j + 1] * (float)kernel[5] + (float)PixelesB[i + 1][j] * (float)kernel[7] + (float)PixelesB[i + 1][j + 1] * (float)kernel[8];
            }
            else if ((i == 0) && (j == Width - 1)) {
              cont = cont +(float)PixelesB[i][j - 1] * (float)kernel[3] + (float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i + 1][j - 1] * (float)kernel[6] +(float)PixelesB[i + 1][j] * (float)kernel[7];
            }
            else if ((i == Height - 1) && (j == 0)) {
              cont = cont + (float)PixelesB[i - 1][j] * (float)kernel[1] + (float)PixelesB[i - 1][j + 1] * (float)kernel[2] + (float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i][j + 1] * (float)kernel[5];
            }
            else if ((i == Height - 1) && (j == Width - 1)) {
              cont = cont + (float)PixelesB[i - 1][j - 1] * (float)kernel[0] + (float)PixelesB[i - 1][j] * (float)kernel[1] + (float)PixelesB[i][j - 1] * (float)kernel[3] +(float)PixelesB[i][j] * (float)kernel[4];
            }
            else if (i == 0) {
              cont = cont + (float)PixelesB[i][j - 1] * (float)kernel[3] + (float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i][j + 1] * (float)kernel[5] + (float)PixelesB[i + 1][j - 1] * (float)kernel[6] +(float)PixelesB[i + 1][j] * (float)kernel[7] + (float)PixelesB[i + 1][j + 1] * (float)kernel[8];
            } 
            else if (j == 0) {
              cont = cont + (float)PixelesB[i - 1][j] * (float)kernel[1] + (float)PixelesB[i - 1][j + 1] * (float)kernel[2] + (float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i][j + 1] * (float)kernel[5] +(float)PixelesB[i + 1][j] * (float)kernel[7] + (float)PixelesB[i + 1][j + 1] * (float)kernel[8];
            }
            else if (i == Height - 1) {
              cont = cont + (float)PixelesB[i - 1][j - 1] * (float)kernel[0] + (float)PixelesB[i - 1][j] * (float)kernel[1] + (float)PixelesB[i - 1][j + 1] * (float)kernel[2] + (float)PixelesB[i][j - 1] * (float)kernel[3] +(float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i][j + 1] * (float)kernel[5];
            }
            else if (j == Width - 1) {
              cont = cont + (float)PixelesB[i - 1][j - 1] * (float)kernel[0] + (float)PixelesB[i - 1][j] * (float)kernel[1] + (float)PixelesB[i][j - 1] * (float)kernel[3] +(float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i + 1][j - 1] * (float)kernel[6] + (float)PixelesB[i + 1][j] * (float)kernel[7];
            }
            else {
              cont = cont + (float)PixelesB[i - 1][j - 1] * (float)kernel[0] + (float)PixelesB[i - 1][j] * (float)kernel[1] + (float)PixelesB[i - 1][j + 1] * (float)kernel[2] + (float)PixelesB[i][j - 1] * (float)kernel[3] +(float)PixelesB[i][j] * (float)kernel[4] + (float)PixelesB[i][j + 1] * (float)kernel[5] + (float)PixelesB[i + 1][j - 1] * (float)kernel[6] + (float)PixelesB[i + 1][j] * (float)kernel[7] + (float)PixelesB[i + 1][j + 1] * (float)kernel[8];
            }

            int cont2 = Math.round(cont);

            if(cont2 <= 0) {
              cont2 = 0;
            }
            else if (cont2 >= 255) {
              cont2 = 255;
            }

            PixelBAux[i][j] = (byte)cont2;
          }
        }

        //Kernel a Matriz Verde
        for(int i = 0; i < Height; i++) {
          for(int j = 0; j < Width; j++) {
            float cont = 0;

            if ((i == 0) && (j == 0)) {
              cont = (float)PixelesG[i][j] * (float)kernel[4] +(float)PixelesG[i][j + 1] * (float)kernel[5] + (float)PixelesG[i + 1][j] * (float)kernel[7] + (float)PixelesG[i + 1][j + 1] * (float)kernel[8];
            }
            else if ((i == 0) && (j == Width - 1)) {
              cont = cont + (float)PixelesG[i][j - 1] * (float)kernel[3] + (float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i + 1][j - 1] * (float)kernel[6] +(float)PixelesG[i + 1][j] * (float)kernel[7];
            }
            else if ((i == Height - 1) && (j == 0)) {
              cont = cont + (float)PixelesG[i - 1][j] * (float)kernel[1] + (float)PixelesG[i - 1][j + 1] * (float)kernel[2] + (float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i][j + 1] * (float)kernel[5];
            }
            else if ((i == Height - 1) && (j == Width - 1)) {
              cont = cont + (float)PixelesG[i - 1][j - 1] * (float)kernel[0] + (float)PixelesG[i - 1][j] * (float)kernel[1] + (float)PixelesG[i][j - 1] * (float)kernel[3] +(float)PixelesG[i][j] * (float)kernel[4];
            } 
            else if (i == 0) {
              cont = cont + (float)PixelesG[i][j - 1] * (float)kernel[3] + (float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i][j + 1] * (float)kernel[5] +(float)PixelesG[i + 1][j - 1] * (float)kernel[6] +(float)PixelesG[i + 1][j] * (float)kernel[7] + (float)PixelesG[i + 1][j + 1] * (float)kernel[8];
            }
            else if (j == 0) {
              cont = cont + (float)PixelesG[i - 1][j] * (float)kernel[1] + (float)PixelesG[i - 1][j + 1] * (float)kernel[2] + (float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i][j + 1] * (float)kernel[5] +(float)PixelesG[i + 1][j] * (float)kernel[7] + (float)PixelesG[i + 1][j + 1] * (float)kernel[8];
            }
            else if (i == Height - 1) {
              cont = cont + (float)PixelesG[i - 1][j - 1] * (float)kernel[0] + (float)PixelesG[i - 1][j] * (float)kernel[1] + (float)PixelesG[i - 1][j + 1] * (float)kernel[2] + (float)PixelesG[i][j - 1] * (float)kernel[3] +(float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i][j + 1] * (float)kernel[5];
            }
            else if (j == Width - 1) {
              cont = cont + (float)PixelesG[i - 1][j - 1] * (float)kernel[0] + (float)PixelesG[i - 1][j] * (float)kernel[1] + (float)PixelesG[i][j - 1] * (float)kernel[3] +(float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i + 1][j - 1] * (float)kernel[6] + (float)PixelesG[i + 1][j] * (float)kernel[7];
            }
            else {
              cont = cont + (float)PixelesG[i - 1][j - 1] * (float)kernel[0] + (float)PixelesG[i - 1][j] * (float)kernel[1] + (float)PixelesG[i - 1][j + 1] * (float)kernel[2] + (float)PixelesG[i][j - 1] * (float)kernel[3] +(float)PixelesG[i][j] * (float)kernel[4] + (float)PixelesG[i][j + 1] * (float)kernel[5] + (float)PixelesG[i + 1][j - 1] * (float)kernel[6] + (float)PixelesG[i + 1][j] * (float)kernel[7] + (float)PixelesG[i + 1][j + 1] * (float)kernel[8];
            }

            int cont2 = Math.round(cont);

            if (cont2 <= 0) {
              cont2 = 0;
            }
            else if(cont2 >= 255) {
              cont2 = 255;
            }

            PixelGAux[i][j] = (byte)cont2;
          }
        }

        //Kernel a Matriz Rojo
        for(int i = 0; i < Height; i++){
          for(int j = 0; j < Width; j++){
            float cont = 0;

            if ((i == 0) && (j == 0)) {
              cont = (float)PixelesR[i][j] * (float)kernel[4] +(float)PixelesR[i][j + 1] * (float)kernel[5] + (float)PixelesR[i + 1][j] * (float)kernel[7] + (float)PixelesR[i + 1][j + 1] * (float)kernel[8];
            }
            else if ((i == 0) && (j == Width - 1)) {
              cont = cont +(float)PixelesR[i][j - 1] * (float)kernel[3] + (float)PixelesR[i][j] * (float)kernel[4] + (float)PixelesR[i + 1][j - 1] * (float)kernel[6] +(float)PixelesR[i + 1][j] * (float)kernel[7];
            }
            else if ((i == Height - 1) && (j == 0)) {
              cont = cont + (float)PixelesR[i - 1][j] * (float)kernel[1] + (float)PixelesR[i - 1][j + 1] * (float)kernel[2] + (float)PixelesR[i][j] * (float)kernel[4] + (float)PixelesR[i][j + 1] * (float)kernel[5];
            }
            else if ((i == Height - 1) && (j == Width - 1)) {
              cont = cont + (float)PixelesR[i - 1][j - 1] * (float)kernel[0] + (float)PixelesR[i - 1][j] * (float)kernel[1] + (float)PixelesR[i][j - 1] * (float)kernel[3]+(float)PixelesR[i][j] * (float)kernel[4];
            } 
            else if (i == 0) {
              cont = cont + (float)PixelesR[i][j - 1] * (float)kernel[3] + (float)PixelesR[i][j] * (float)kernel[4] + (float)PixelesR[i][j + 1] * (float)kernel[5] +(float)PixelesR[i + 1][j - 1] * (float)kernel[6] +(float)PixelesR[i + 1][j] * (float)kernel[7] + (float)PixelesR[i + 1][j + 1] * (float)kernel[8];
            }
            else if (j == 0) {
              cont = cont + (float)PixelesR[i - 1][j] * (float)kernel[1] + (float)PixelesR[i - 1][j + 1] * (float)kernel[2] + (float)PixelesR[i][j] * (float)kernel[4] +(float)PixelesR[i][j+1] * (float)kernel[5]+(float)PixelesR[i + 1][j] * (float)kernel[7] + (float)PixelesR[i + 1][j + 1] * (float)kernel[8];
            }
            else if (i == Height - 1) {
              cont = cont + (float)PixelesR[i - 1][j - 1] * (float)kernel[0] + (float)PixelesR[i - 1][j] * (float)kernel[1] + (float)PixelesR[i - 1][j + 1] * (float)kernel[2] + (float)PixelesR[i][j - 1] * (float)kernel[3] +(float)PixelesR[i][j] * (float)kernel[4] + (float)PixelesR[i][j + 1] * (float)kernel[5];
            }
            else if (j == Width - 1) {
              cont = cont + (float)PixelesR[i - 1][j - 1] * (float)kernel[0] + (float)PixelesR[i - 1][j] * (float)kernel[1] + (float)PixelesR[i][j - 1] * (float)kernel[3]+(float)PixelesR[i][j] * (float)kernel[4] + (float)PixelesR[i + 1][j-1] * (float)kernel[6] + (float)PixelesR[i + 1][j] * (float)kernel[7];
            }else{
              cont = cont + (float)PixelesR[i - 1][j - 1] * (float)kernel[0] + (float)PixelesR[i - 1][j] * (float)kernel[1] + (float)PixelesR[i - 1][j + 1] * (float)kernel[2] + (float)PixelesR[i][j - 1] * (float)kernel[3] + (float)PixelesR[i][j] * (float)kernel[4] + (float)PixelesR[i][j + 1] * (float)kernel[5] + (float)PixelesR[i + 1][j - 1] * (float)kernel[6] + (float)PixelesR[i + 1][j] * (float)kernel[7] + (float)PixelesR[i + 1][j + 1] * (float)kernel[8];
            }

            int cont2 = Math.round(cont);

            if(cont2 <= 0){
              cont2 = 0;
            } 
            else if(cont2 >= 255) {
              cont2 = 255;
            }

            PixelRAux[i][j] = (byte)cont2;
          }
        }
        //Imagen con kernel
        Aux = 54;

        for (int j = 0; j < Height; j++) {
          for (int i = 0; i < Width; i++) {
            Datos2[Aux] = PixelBAux[j][i];
            Aux++;
            Datos2[Aux] = PixelGAux[j][i];
            Aux++;
            Datos2[Aux] = PixelRAux[j][i];
            Aux++;
          }
        }
      
        FileOutputStream Imagen = new FileOutputStream(NombreI.substring(0, NombreI.indexOf(".")) + "Kernel" + ".bmp");
        Imagen.write(Datos2);
        Imagen.close();

      }
      else {
        System.out.println("¡Tipo de archivo ingresado incorrecto!");
      }
    }
    catch (FileNotFoundException Error) {
      System.out.println("*No se ha enconTrado el archivo*");
    }
  }
}