// import java.sql.Connection;
// import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    TableView<Mahasiswa> tableView = new TableView<Mahasiswa>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Table Database");

        TableColumn<Mahasiswa, Integer> columnID = new TableColumn<>("ID");
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Mahasiswa, String> columnNAMA = new TableColumn<>("Nama");
        columnNAMA.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<Mahasiswa, String> columnNIM = new TableColumn<>("NIM");
        columnNIM.setCellValueFactory(new PropertyValueFactory<>("nim"));

        TableColumn<Mahasiswa, String> columnJurusan = new TableColumn<>("Jurusan");
        columnJurusan.setCellValueFactory(new PropertyValueFactory<>("jurusan"));

        tableView.getColumns().add(columnID);
        tableView.getColumns().add(columnNAMA);
        tableView.getColumns().add(columnNIM);
        tableView.getColumns().add(columnJurusan);


        // Button
        ToolBar toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: #a8dadc");

        Button button1 = new Button("Add");
        // button1.setStyle("-fx-background-color: #a8dadc");
        toolBar.getItems().add(button1);
        button1.setOnAction(e -> Add());

        Button button2 = new Button("Edit");
        // button2.setStyle("-fx-background-color: #457b9d");
        toolBar.getItems().add(button2);
        button2.setOnAction(e -> Edit());

        Button button3 = new Button("Delete");
        button3.setStyle("-fx-background-color: #e63946");
        toolBar.getItems().add(button3);
        button3.setOnAction(e -> Delete());

        Button button4 = new Button("Refresh");
        button4.setStyle("-fx-background-color: #8aea92");
        toolBar.getItems().add(button4);
        button4.setOnAction(e -> re());

        load();
        VBox vbox = new VBox(tableView, toolBar);
        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);

        primaryStage.show();

        Statement stmt;

        // String url = "jdbc:mysql://localhost:3306/db_akademik";
        // String user = "root";
        // String pass = "";

        try {
            Database db = new Database();
            stmt = db.conn.createStatement();
            ResultSet record = stmt.executeQuery("select * from tabel_mahasiswa");
            tableView.getItems().clear();

            while (record.next()) {
                tableView.getItems().add(new Mahasiswa(record.getInt("id"), record.getString("nama"),
                        record.getString("nim"), record.getString("jurusan")));
            }

        } catch (SQLException e) {
            System.out.println("koneksi gagal");
        }

    }

    public void load() {
        Statement stmt;
        tableView.getItems().clear();
        try {
            Database db = new Database();
            // Connection conn = DriverManager.getConnection(url, user, pass);
            stmt = db.conn.createStatement();
            ResultSet record = stmt.executeQuery("select * from tabel_mahasiswa");

            while (record.next()) {
                tableView.getItems().add(new Mahasiswa(record.getInt("id"), record.getString("nama"),
                        record.getString("nim"), record.getString("jurusan")));
            }

            stmt.close();
            db.conn.close();
        } catch (SQLException e) {
            System.out.println("");
        }
    }

    // Bagian add
    public void Add() {
        Stage addStage = new Stage();
        Button save = new Button("Simpan");

        addStage.setTitle("Add Data");

        TextField namaField = new TextField();
        TextField nimField = new TextField();
        TextField jurusanField = new TextField();
        Label labelnama = new Label("Nama");
        Label labelnim = new Label("NIM");
        Label labeljurusan = new Label("Jurusan");

        VBox hbox1 = new VBox(5, labelnama, namaField);
        VBox hbox2 = new VBox(5, labelnim, nimField);
        VBox hbox3 = new VBox(5, labeljurusan, jurusanField);
        VBox vbox = new VBox(20, hbox1, hbox2, hbox3, save);

        Scene scene = new Scene(vbox, 500, 500);

        save.setOnAction(e -> {
            Database db = new Database();
            try {
                Statement state = db.conn.createStatement();
                String sql = "insert into tabel_mahasiswa set Nama='%s', NIM='%s', Jurusan='%s'";
                sql = String.format(sql, namaField.getText(), nimField.getText(), jurusanField.getText());
                state.execute(sql);
                addStage.close();
                load();
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        });

        addStage.setScene(scene);
        addStage.show();
    }

    // Bagian Edit
    public void Edit() {
        Stage addStage = new Stage();
        Button save = new Button("Simpan");

        addStage.setTitle("Edit Data");

        TextField idField = new TextField();
        TextField namaField = new TextField();
        TextField nimField = new TextField();
        TextField jurusanField = new TextField();

        Label labelid = new Label("Pilih ID yang akan diubah");
        Label labelnama = new Label("Nama");
        Label labelnim = new Label("NIM");
        Label labeljurusan = new Label("Jurusan");

        VBox hbox1 = new VBox(5, labelid, idField);
        VBox hbox2 = new VBox(5, labelnama, namaField);
        VBox hbox3 = new VBox(5, labelnim, nimField);
        VBox hbox4 = new VBox(5, labeljurusan, jurusanField);
        VBox vbox = new VBox(20, hbox1, hbox2, hbox3, hbox4, save);

        Scene scene = new Scene(vbox, 500, 500);

        save.setOnAction(e -> {
            Database db = new Database();
            try {
                Statement state = db.conn.createStatement();
                String sql = "update tabel_mahasiswa set Nama='%s', NIM='%s', Jurusan='%s' where ID='%s'";
                sql = String.format(sql, namaField.getText(), nimField.getText(), jurusanField.getText(),
                        idField.getText());
                state.execute(sql);
                addStage.close();
                load();
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        });

        addStage.setScene(scene);
        addStage.show();
    }

    // Bagian Delete
    public void Delete() {
        Stage addStage = new Stage();
        Button save = new Button("Hapus");

        addStage.setTitle("Delete Data");

        TextField idField = new TextField();

        Label labelid = new Label("Pilih Id");

        VBox hbox1 = new VBox(5, labelid, idField);
        VBox vbox = new VBox(20, hbox1, save);

        Scene scene = new Scene(vbox, 500, 500);

        save.setOnAction(e -> {
            Database db = new Database();
            try {
                Statement state = db.conn.createStatement();
                String sql = "delete from tabel_mahasiswa where id='%s'";
                sql = String.format(sql, idField.getText());
                state.execute(sql);
                addStage.close();
                load();
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        });

        addStage.setScene(scene);
        addStage.show();
    }

    public void re() {
        Database db = new Database();
        try {
            Statement state = db.conn.createStatement();
            String sql = "ALTER TABLE tabel_mahasiswa DROP id";
            sql = String.format(sql);
            state.execute(sql);
            re2();

        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println();
        }
    }

    public void re2() {
        Database db = new Database();
        try {
            Statement state = db.conn.createStatement();
            String sql = "ALTER TABLE tabel_mahasiswa ADD id INT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST";
            sql = String.format(sql);
            state.execute(sql);
            load();
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println();
        }
    }
}