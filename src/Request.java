import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

public class Request {

    // When creating a new player must store the token created by the PasswordUtils
    // Instead of Username we store username and the token

    public static class PlayerTable{
        public String playerName;
        public int playerScore;
        public String password;

        PlayerTable(String playerName,int playerScore,String password){
            this.playerName = playerName;
            this.playerScore = playerScore;
            this.password = password;
        }

    }

    public static ArrayList<PlayerTable> getPlayersData() {
        ArrayList<PlayerTable> players = new ArrayList<>();
        try {
            String apiUrl = "https://script.google.com/macros/s/AKfycbx9OKhZ8JxLsnDP255FHX1ImLgWsftlaESGsvrENxnRQKqpm90H5Fvmzq6s533Jwu9O/exec";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String responseText = response.toString();
            JSONObject jsonObject = new JSONObject(responseText);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject user = jsonArray.getJSONObject(i);
                String playerName = user.getString("name");
                int playerScore = user.getInt("highscore");
                String password = user.getString("password");
                PlayerTable player = new PlayerTable(playerName,playerScore,password);
                players.add(player);
            }



        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was a problem retrieving the data from the server.");
        }
        return players;

    }

    public static boolean createAnewPlayer(String playerName,String password) throws UnsupportedEncodingException {
        String token = PasswordUtils.generateToken(playerName,password);
        String hashedPassword = PasswordUtils.hashPassword(password);

        String encodedName = URLEncoder.encode(playerName, "UTF-8");
        String encodedPassword = URLEncoder.encode(hashedPassword, "UTF-8");
        String encodedToken = URLEncoder.encode(token, "UTF-8");

        try {
            String apiUrl = "https://script.google.com/macros/s/AKfycbx9OKhZ8JxLsnDP255FHX1ImLgWsftlaESGsvrENxnRQKqpm90H5Fvmzq6s533Jwu9O/exec";
            String queryParams = String.format("?method=post&name=%s&password=%s&token=%s",encodedName,encodedPassword,encodedToken);

            URL url = new URL(apiUrl + queryParams);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);  // Allow for output (even if no body)
            connection.setRequestProperty("Content-Length", "0");  // Set Content-Length to 0
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Explicitly write empty body if needed
            OutputStream os = connection.getOutputStream();
            os.write(new byte[0]); // Send empty body
            os.close();

            // Read the server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String responseText = response.toString();
            JSONObject jsonObject = new JSONObject(responseText);

            if(responseText.contains("status") && jsonObject.getString("status").equals("error")){
                JOptionPane.showMessageDialog(null,jsonObject.getString("message"));
                return false;
            }

            return true;




        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return false;
    }

    public static boolean updatePlayerScore(String playerName,String token,int playerScore) throws UnsupportedEncodingException {

            String encodedName = URLEncoder.encode(playerName, "UTF-8");
            String encodedToken = URLEncoder.encode(token, "UTF-8");

            try {
                String apiUrl = "https://script.google.com/macros/s/AKfycbx9OKhZ8JxLsnDP255FHX1ImLgWsftlaESGsvrENxnRQKqpm90H5Fvmzq6s533Jwu9O/exec";
                String queryParams = String.format("?method=put&name=%s&&token=%s&highscore=%d",encodedName,encodedToken,playerScore);

                URL url = new URL(apiUrl + queryParams);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);  // Allow for output (even if no body)
                connection.setRequestProperty("Content-Length", "0");  // Set Content-Length to 0
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Explicitly write empty body if needed
                OutputStream os = connection.getOutputStream();
                os.write(new byte[0]); // Send empty body
                os.close();

                // Read the server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String responseText = response.toString();
                JSONObject jsonObject = new JSONObject(responseText);

                if(responseText.contains("status") && jsonObject.getString("status").equals("error")){
                    JOptionPane.showMessageDialog(null,jsonObject.getString("message"));
                    return false;
                }

                return true;




            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
            return false;
        }

    public static void versionControl(){

        try {
            String apiUrl = "https://script.google.com/macros/s/AKfycbx9OKhZ8JxLsnDP255FHX1ImLgWsftlaESGsvrENxnRQKqpm90H5Fvmzq6s533Jwu9O/exec?req=getVersion";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String responseText = response.toString();
            JSONObject jsonObject = new JSONObject(responseText);

            String versionNumber = jsonObject.getString("versionNumber");
            String versionMessage = jsonObject.getString("versionMessage");

            if(!versionNumber.equals(GameSettings.version_number)) JOptionPane.showMessageDialog(null,"There is a new version available\nYou can download it from the same repository\nVersion : "+versionNumber+"\n"+versionMessage);

        } catch (Exception e) {
            System.out.println("There was a problem retrieving the data from the server.");
        }

    }


}
