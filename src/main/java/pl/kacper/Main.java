package pl.kacper;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:h2:./db/bazaProdukty;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        String query1 = """
                SELECT *\s
                FROM PRODUKTY
               \s""";
        String query2 = """
                UPDATE PRODUKTY\s
                SET STAN_NA_MAGAZYNIE = STAN_NA_MAGAZYNIE - ?\s
                WHERE ID = ?
               \s""";
        String query3 = """
                SELECT p.STAN_NA_MAGAZYNIE
                FROM PRODUKTY p
                WHERE p.ID = ?
                """;
        String query4 = """
                SELECT p.NAZWA, p.STAN_NA_MAGAZYNIE
                FROM PRODUKTY p
                WHERE ID = ?
                """;


        try (Scanner scanner = new Scanner(System.in)){

            // Łączenie z bazą
            try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){

                // Segment1
                try(PreparedStatement stmt = conn.prepareStatement(query1)){
                    try(ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("ID");
                            String nazwa = rs.getString("NAZWA");
                            double cena = rs.getDouble("CENA");
                            int stan = rs.getInt("STAN_NA_MAGAZYNIE");

                            System.out.println(
                                    "ID:       " + id +
                                            "\nNazwa:    " + nazwa +
                                            "\nCena:     " + cena +
                                            "\nStan:     " + stan +
                                            "\n------------------------------------\n");
                        }
                    }
                }

                // Segment2
                System.out.print("Podaj id produktu, który chcesz zakupić: ");
                int kupnoId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Ile sztuk chcesz zakupić?: ");
                int iloscKupionego = scanner.nextInt();
                scanner.nextLine();
                int iloscWMagazynie = 0;

                try(PreparedStatement stmt = conn.prepareStatement(query3)){
                    stmt.setInt(1, kupnoId);
                    try(ResultSet rs = stmt.executeQuery()){
                        while (rs.next()){
                            iloscWMagazynie = rs.getInt("STAN_NA_MAGAZYNIE");
                        }
                    }
                }

                if (iloscWMagazynie < iloscKupionego){
                    throw new NotEnoughtProductsException("W magazynie nie ma wystarczającej ilości produktów");
                }

                try(PreparedStatement stmt = conn.prepareStatement(query2)){
                    stmt.setInt(1, iloscKupionego);
                    stmt.setInt(2, kupnoId);

                    System.out.println("\nZmiana wartości...");
                    int iloscWierszyZmienionych = stmt.executeUpdate();
                    System.out.println("Stan magazynu został zaktualizowany!");
                    System.out.println("Zmianie uległ: " + iloscWierszyZmienionych + " wiersz!\n");
                }

                // Segment 3 dla mnie, żeby zobaczyć czy się zmieniło
                try(PreparedStatement stmt = conn.prepareStatement(query4)){
                    stmt.setInt(1, kupnoId);
                    try(ResultSet rs = stmt.executeQuery()){
                        while (rs.next()){
                            String nazwa = rs.getString("NAZWA");
                            int stanWMagazyniePo = rs.getInt("STAN_NA_MAGAZYNIE");

                            System.out.println( "\nNazwa:               " + nazwa +
                                    "\nStan w magazynie:    " + stanWMagazyniePo +
                                    "\n---------------------------\n");
                        }
                    }
                }
            }catch (SQLException e){
                System.out.println("BŁĄD: " + e.getMessage());
            }catch (NotEnoughtProductsException e){
                System.out.println(e.getMessage());
            }
        }catch (InputMismatchException e){
            System.out.println("Błędna wartość");
        }
    }
}