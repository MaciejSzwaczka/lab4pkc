package sample;

import org.sqlite.SQLiteConfig;

import java.io.*;
import java.security.acl.LastOwnerException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by maciejszwaczka on 17.11.2018.
 */
public class DBAdapter {
    public static ArrayList<Double> l = new ArrayList<Double>();
    public static ArrayList<Localization> localizations = new ArrayList<Localization>();
    private static Connection connection;
    private HashSet<Integer> loadedAreas;
    public static double minx=Double.MAX_VALUE, miny=Double.MAX_VALUE,maxx=Double.MIN_VALUE,maxy=Double.MIN_VALUE;
    public DBAdapter (String dbNazwa)
    {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enableLoadExtension(true);
            connection = (Connection) DriverManager.getConnection("jdbc:sqlite:" +dbNazwa, config.toProperties());
        } catch (SQLException e) {
            System.out.println("Wyjatek SQL: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Nie znaleziono klasy: " + cE.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static double[] getXRangeParam()
    {
        Statement st = null;
        try {
            st = connection.createStatement();
            ResultSet rset = st.executeQuery("SELECT MIN(x), MAX(X) FROM coords;");
            double minX = rset.getDouble(1);
            double maxX = rset.getDouble(2);
            double[] result = new double[2];
            result[0] = minX;
            result[1] = maxX;
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getYRangeParam()
    {
        Statement st = null;
        try {
            st = connection.createStatement();
            ResultSet rset = st.executeQuery("SELECT MIN(y), MAX(y) FROM coords;");
            double minX = rset.getDouble(1);
            double maxX = rset.getDouble(2);
            double[] result = new double[2];
            result[0] = minX;
            result[1] = maxX;
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Localization> getCoords()
    {
        Statement st = null;
        List<Localization> locs= new ArrayList<Localization>();
        try {
            st = connection.createStatement();
            ResultSet rset = st.executeQuery("SELECT * FROM coords;");
            int rowCount = 0;
            while(rset.next()) {   // Move the cursor to the next row, return false if no more row
                double x = rset.getDouble("x");
                double y = rset.getDouble("y");
                double h   = rset.getDouble("h");
                locs.add(new Localization(x,y,h));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locs;
    }


    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException{

        DBAdapter db = new DBAdapter("baza");

        File file = new File("C:\\Users\\maciejszwaczka\\Desktop\\mc-4_lab_mc_10\\dane_politechnika.txt");

        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\maciejszwaczka\\Desktop\\mc-4_lab_mc_10\\dane_politechnika.txt"));
        String line;
        while((line=reader.readLine())!=null)
        {
            String[] coords=line.split("   ");
            Localization localization=new Localization(Double.parseDouble(coords[0]),
                    Double.parseDouble(coords[1]),
                    Double.parseDouble(coords[2]));
            localizations.add(localization);
            if(localization.x>maxx)
            {
                maxx=localization.x;
            }
            if(localization.x<minx)
            {
                minx=localization.x;
            }
            if(localization.y>maxy)
            {
                maxy=localization.y;
            }
            if(localization.y<miny)
            {
                miny=localization.y;
            }
        }
        Statement st = db.connection.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS coords (id INTEGER PRIMARY KEY AUTOINCREMENT, x REAL, y REAL, h REAL);");
        st.close();
        AWTControlDemo c = new AWTControlDemo();
        c.showCanvasDemo();
        Statement statement=connection.createStatement();
        for(int i = 0; i < localizations.size(); i++)
        {
            statement.execute("INSERT INTO coords (x, y, h) VALUES (" +
                    localizations.get(i).x + "," +
                    localizations.get(i).y + "," +
                    localizations.get(i).z + ");");

        }
        statement.close();
        getCoords();
        getXRangeParam();
        getYRangeParam();
        c.showCanvasDemo();
        st.close();

    }
}
