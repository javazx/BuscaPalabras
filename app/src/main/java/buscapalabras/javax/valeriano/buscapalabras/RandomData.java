package buscapalabras.javax.valeriano.buscapalabras;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by israe on 02/10/2017.
 */

public class RandomData {
    private String[] direccion = new String[4];
    private String[] datos = new String[100];
    private String palabrasBuscar = "";
    private String[] alfabeto = new String[] {
            "A","B","C","D","E","F","G","H","I","J",
            "K","L","M","N","O","P","Q","R","S","T",
            "U","V","W","X","Y","Z"};
    private  Random randData = new Random();
    private ArrayList<Integer> posiciones = new ArrayList<Integer>();
    private final int GRIDTOTAL = 100;
    private final int LETRASBUSCAR = 3;
    int direccionXY;

    public RandomData(){
        direccion[0] = "izquierda";
        direccion[1] = "derecha";
        direccion[2] = "arriba";
        direccion[3] = "abajo";

        for(int i=0; i<100; i++){
            int letra = randData.nextInt(26);
            datos[i] = alfabeto[letra];
        }
    }

    public void setPalabras(ArrayList<String> palabras){
        ArrayList<String> noBuscar = new ArrayList<String>();
        palabrasBuscar = "";
        for(int i =0; i< LETRASBUSCAR ;i++){
            String palabra = palabras.get(randData.nextInt(30));
            if(i == 0){
                noBuscar.add(palabra);
            }else{
                palabra = palabras.get(randData.nextInt(30));
                do{
                    if(noBuscar.contains(palabra)){
                        palabra = palabras.get(randData.nextInt(30));
                    }else{
                        noBuscar.add(palabra);
                        break;
                    }
                }while (true);
            }
            int posicionX = randData.nextInt(10);
            int posicionY = randData.nextInt(10);
            palabrasBuscar += "-" + palabra.toString();
            Log.d("Palabra",palabra.toString());


            String resultados = "Error";
            int sumError = 0;
            do {
                String resultDireccion = direccion[randData.nextInt(4)].toString();
                Log.d("Direccion", resultDireccion);
                if (resultDireccion.equalsIgnoreCase("derecha")) {
                    resultados = direccionDerecha(posicionX, posicionY, palabra);
                } else if (resultDireccion.equalsIgnoreCase("izquierda")) {
                    resultados = direccionIzquierda(posicionX, posicionY, palabra);
                } else if (resultDireccion.equalsIgnoreCase("arriba")) {
                    resultados = direccionArriba(posicionX, posicionY, palabra);
                } else if (resultDireccion.equalsIgnoreCase("abajo")) {
                    resultados = direccionAbajo(posicionX, posicionY, palabra);
                }
                Log.d("Palabra: " , palabra.toString());
                Log.d("PosicionX",String.valueOf(posicionX));
                Log.d("PosicionY", String.valueOf(posicionY));
                Log.d("Resultado",resultados.toString());
                sumError = (sumError + 1);
                if(sumError >= 4){
                    posicionX = randData.nextInt(10);
                    posicionY = randData.nextInt(10);
                    sumError = 0;
                }
            } while(resultados.equalsIgnoreCase("Error"));

        }
    }

    public String direccionDerecha(int x, int y, String palabra){
        int palabraCount = palabra.length();
        if((x + palabraCount) > 10) {
            return "Error";
        }else{
            int posX = x;
            int posY = y * 10;
            int posReal = posX + posY;
            for (int i = 0;i < palabra.length(); i++){
                if(i == 0){
                    if(posiciones.size() == 0){
                        posiciones.add(posReal);
                    }else if(posiciones.contains(posReal)){
                        return "Error";
                    }else{
                        posiciones.add(posReal);
                    }
                    datos[posReal] = String.valueOf(palabra.charAt(i));
                }else{
                    if(posiciones.contains((posReal + i))){
                        return "Error";
                    }else{
                        posiciones.add((posReal + i));
                    }
                   // posiciones.add((posReal + i));
                    datos[(posReal + i)] = String.valueOf(palabra.charAt(i));
                }
            }
        }
        return "OK";
    }


    public String direccionIzquierda(int x, int y, String palabra){
        int palabraCount = palabra.length();
        if((x - palabraCount) < 0) {
            return "Error";
        }else{
            int posX = x;
            int posY = y * 10;
            int posReal = posX + posY;
            for (int i = 0;i < palabra.length(); i++){
                if(i == 0){
                    if(posiciones.size() == 0){
                        posiciones.add(posReal);
                    }else if(posiciones.contains(posReal)){
                        return "Error";
                    }else{
                        posiciones.add(posReal);
                    }
                    //posiciones.add(posReal);
                    datos[posReal] = String.valueOf(palabra.charAt(i));
                }else{
                    if(posiciones.contains((posReal - i))){
                        return "Error";
                    }else{
                        posiciones.add((posReal - i));
                    }
                    datos[(posReal - i)] = String.valueOf(palabra.charAt(i));
                }
            }
        }
        return "OK";
    }


    public String direccionArriba(int x, int y, String palabra){
        int palabraCount = palabra.length();
        if((y - palabraCount) < 0) {
            return "Error";
        }else{
            int posX = x;
            int posY = y * 10;
            int posReal = posX + posY;
            for (int i = 0;i < palabra.length(); i++){
                if(i == 0){
                    if(posiciones.size() == 0){
                        posiciones.add(posReal);
                    }else if(posiciones.contains(posReal)){
                        return "Error";
                    }else{
                        posiciones.add(posReal);
                    }
                    //posiciones.add(posReal);
                    datos[posReal] = String.valueOf(palabra.charAt(i));
                }else{
                    if(posiciones.contains(posReal - (i * 10))){
                        return "Error";
                    }else{
                        posiciones.add((posReal - (i * 10)));
                    }
                    datos[(posReal - (i * 10))] = String.valueOf(palabra.charAt(i));
                }
            }
        }
        return "OK";
    }


    public String direccionAbajo(int x, int y, String palabra){
        int palabraCount = palabra.length();
        if((y + palabraCount) > 10) {
            return "Error";
        }else{
            int posX = x;
            int posY = y * 10;
            int posReal = posX + posY;
            for (int i = 0;i < palabra.length(); i++){
                if(i == 0){
                    if(posiciones.size() == 0){
                        posiciones.add(posReal);
                    }else if(posiciones.contains(posReal)){
                        return "Error";
                    }else{
                        posiciones.add(posReal);
                    }
                    //posiciones.add(posReal);
                    datos[posReal] = String.valueOf(palabra.charAt(i));
                }else{
                    if(posiciones.contains((posReal + (i * 10)))){
                        return "Error";
                    }else{
                        posiciones.add((posReal + (i * 10)));
                    }
                    datos[(posReal + (i * 10))] = String.valueOf(palabra.charAt(i));
                }
            }
        }
        return "OK";
    }

    public String[] getDatos(){
        return datos;
    }

    public String getPalabras(){
        return palabrasBuscar;
    }

}
