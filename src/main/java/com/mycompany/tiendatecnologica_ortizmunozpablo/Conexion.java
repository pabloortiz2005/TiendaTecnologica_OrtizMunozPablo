package com.mycompany.tiendatecnologica_ortizmunozpablo;

import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Conexion {

    // Método para obtener la conexión con la base de datos
    public static Connection getConnection() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/tiendaT";
            String user = "root";  
            String password = ""; 
            // Conectar con la base de datos
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new SQLException("Error al conectar con la base de datos", e);
        }
    }

    // Método para leer el archivo JSON
    public static JSONObject leerJSON() {
        try {
            JSONParser parser = new JSONParser();
            // Ruta al archivo JSON
            Object obj = parser.parse(new FileReader("src/main/java/com/mycompany/tiendatecnologica_ortizmunozpablo/Conexiones/tienda.json"));
            // Convierte el objeto a JSONObject
            return (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para insertar categorías y productos en la base de datos
    public static void insertarCategoriasYProductos(JSONObject json) throws SQLException {
        if (json == null) {
            System.out.println("El JSON está vacío o no se cargó correctamente.");
            return;
        }

        // Accedemos a la clave "tienda" que contiene "categorias"
        JSONObject tienda = (JSONObject) json.get("tienda");
        if (tienda == null) {
            System.out.println("No se encontró la clave 'tienda' en el JSON.");
            return;
        }

        // Accedemos al array "categorias" dentro de "tienda"
        JSONArray categorias = (JSONArray) tienda.get("categorias");
        if (categorias == null) {
            System.out.println("No se encontró el array 'categorias' en el JSON.");
            return;
        }

        Connection conn = getConnection();
        try {
            // Inserción de categorías
            for (Object categoriaObj : categorias) {
                JSONObject categoria = (JSONObject) categoriaObj;
                String nombreCategoria = (String) categoria.get("nombre");

                String query = "INSERT INTO categoria (nombre) VALUES (?)";
                try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, nombreCategoria);
                    pst.executeUpdate();

                    // Obtener el ID de la categoría insertada
                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        if (rs.next()) {
                            int categoriaId = rs.getInt(1);
                            JSONArray productos = (JSONArray) categoria.get("productos");
                            for (Object productoObj : productos) {
                                JSONObject producto = (JSONObject) productoObj;
                                insertarProducto(conn, producto, categoriaId);
                            }
                        }
                    }
                }
            }
        } finally {
            conn.close();
        }
    }

    // Método para insertar un producto
    private static void insertarProducto(Connection conn, JSONObject producto, int categoriaId) throws SQLException {
        int idProducto = ((Long) producto.get("id")).intValue();
        String nombreProducto = (String) producto.get("nombre");
        Double precioProducto = (Double) producto.get("precio");
        String descripcionProducto = (String) producto.get("descripcion");
        int inventario = ((Long) producto.get("inventario")).intValue();
        
         if (existeProducto(conn, idProducto)) {
        System.out.println("El producto con ID " + idProducto + " ya existe. No se insertará.");
        return;  // Si el producto ya existe, no lo insertamos
    }

        // Preparar y ejecutar la consulta de inserción de producto
        String query = "INSERT INTO producto (id, nombre, precio, descripcion, inventario, categoria_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idProducto);
            pst.setString(2, nombreProducto);
            pst.setDouble(3, precioProducto);
            pst.setString(4, descripcionProducto);
            pst.setInt(5, inventario);
            pst.setInt(6, categoriaId);
            pst.executeUpdate();
        }
    }

    // Método para insertar usuarios en la base de datos
    public static void insertarUsuarios(JSONObject json) throws SQLException {
        if (json == null) {
            System.out.println("El JSON está vacío o no se cargó correctamente.");
            return;
        }

        // Acceder al objeto "tienda"
        JSONObject tienda = (JSONObject) json.get("tienda");
        if (tienda == null) {
            System.out.println("No se encontró el objeto 'tienda' en el JSON.");
            return;
        }

        // Obtener el array "usuarios" dentro de "tienda"
        JSONArray usuarios = (JSONArray) tienda.get("usuarios");
        if (usuarios == null) {
            System.out.println("La clave 'usuarios' no se encontró o está vacía.");
            return;
        }

        // Procesar los usuarios
        Connection conn = getConnection();
        try {
            for (Object usuarioObj : usuarios) {
                JSONObject usuario = (JSONObject) usuarioObj;
                String nombreUsuario = (String) usuario.get("nombre");
                String emailUsuario = (String) usuario.get("email");
                JSONObject direccion = (JSONObject) usuario.get("direccion");

                String calle = (String) direccion.get("calle");
                Long numero = (Long) direccion.get("numero");
                String ciudad = (String) direccion.get("ciudad");
                String pais = (String) direccion.get("pais");

                // Verificar si el usuario ya existe por email
                if (existeRegistro(conn, "usuario", "email", emailUsuario)) {
                    System.out.println("El usuario con el email " + emailUsuario + " ya existe. No se insertará.");
                    continue; // Si el email existe, omitir la inserción de este usuario
                }

                // Insertar usuario
                String query = "INSERT INTO usuario (nombre, email, calle, numero, ciudad, pais) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = conn.prepareStatement(query)) {
                    pst.setString(1, nombreUsuario);
                    pst.setString(2, emailUsuario);
                    pst.setString(3, calle);
                    pst.setLong(4, numero);
                    pst.setString(5, ciudad);
                    pst.setString(6, pais);
                    pst.executeUpdate();

                    // Insertar historial de compras solo si el producto y usuario existen
                    JSONArray historialCompras = (JSONArray) usuario.get("historialCompras");
                    for (Object compraObj : historialCompras) {
                        JSONObject compra = (JSONObject) compraObj;
                        int productoId = ((Long) compra.get("productoId")).intValue();
                        int cantidad = ((Long) compra.get("cantidad")).intValue();
                        String fecha = (String) compra.get("fecha");

                        // Verificar si el producto existe
                        if (existeProducto(conn, productoId)) {
                            // Insertar historial de compras
                            String historialQuery = "INSERT INTO historial_compras (usuario_id, producto_id, cantidad, fecha) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement pstCompra = conn.prepareStatement(historialQuery)) {
                                pstCompra.setInt(1, getUsuarioId(conn, emailUsuario));
                                pstCompra.setInt(2, productoId);
                                pstCompra.setInt(3, cantidad);
                                pstCompra.setString(4, fecha);
                                pstCompra.executeUpdate();
                            }
                        } else {
                            System.out.println("El producto con ID " + productoId + " no existe. No se insertó en el historial de compras.");
                        }
                    }
                }
            }
        } finally {
            conn.close();
        }
    }

    // Método para obtener el ID del usuario por email
    private static int getUsuarioId(Connection conn, String emailUsuario) throws SQLException {
        String query = "SELECT id FROM usuario WHERE email = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, emailUsuario);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Usuario no encontrado: " + emailUsuario);
            }
        }
    }

    // Método para verificar si un producto existe en la base de datos
    private static boolean existeProducto(Connection conn, int productoId) throws SQLException {
        String query = "SELECT id FROM producto WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, productoId);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        }
    }

    // Método para verificar si un registro existe en una tabla
    public static boolean existeRegistro(Connection conn, String tabla, String columna, String valor) throws SQLException {
        String query = "SELECT 1 FROM " + tabla + " WHERE " + columna + " = ? LIMIT 1";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, valor);
            ResultSet rs = pst.executeQuery();
            return rs.next();  // Si hay al menos un registro, devuelve true
        }
    }

    // Método para obtener el nombre del producto por su ID
    public static String obtenerNombreProductoPorId(Connection conn, int productoId) throws SQLException {
        String nombreProducto = null;

        // Consulta SQL para obtener el nombre del producto basado en su ID
        String query = "SELECT nombre FROM producto WHERE id = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, productoId);  // Establecer el ID del producto en la consulta
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    nombreProducto = rs.getString("nombre") ;  // Obtener el nombre del producto
                }
            }
        }

        return nombreProducto;  // Retorna el nombre o null si no se encuentra
    }
    
    public static Usuario obtenerUsuarioPorNombre(Connection conn, String nombre) throws SQLException {
    String query = "SELECT nombre, id, email, calle, numero, ciudad, pais FROM usuario WHERE nombre = ?";

    try (PreparedStatement pst = conn.prepareStatement(query)) {
        pst.setString(1, nombre);
        
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return new Usuario(
                    rs.getString("nombre"),
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("calle"),
                    rs.getString("numero"),
                    rs.getString("ciudad"),
                    rs.getString("pais")
                );
            }
        }
    }

    return null; // Retorna null si no se encuentra ningún usuario
}
    public static List<Compra> obtenerHistorialComprasPorUsuario(Connection conn, int usuarioId) throws SQLException {
    String query = "SELECT id, usuario_id, producto_id, cantidad, fecha FROM historial_compras WHERE usuario_id = ?";
    List<Compra> historialCompras = new ArrayList<>();

    try (PreparedStatement pst = conn.prepareStatement(query)) {
        pst.setInt(1, usuarioId);

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Compra compra = new Compra(
                    rs.getInt("id"),
                    rs.getInt("usuario_id"),
                    rs.getInt("producto_id"),
                    rs.getInt("cantidad"),
                    rs.getDate("fecha")
                );
                historialCompras.add(compra);
            }
        }
    }

    return historialCompras; // Devuelve la lista con el historial de compras
}
    public static int obtenerProductoIdPorNombre(Connection conn, String nombreProducto) throws SQLException {
    String query = "SELECT id FROM producto WHERE nombre = ?";
    try (PreparedStatement pst = conn.prepareStatement(query)) {
        pst.setString(1, nombreProducto);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");  // Retorna el ID del producto
            }
        }
    }
    return -1;  // Retorna -1 si no se encuentra el producto
}


}
