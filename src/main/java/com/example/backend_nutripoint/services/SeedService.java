package com.example.backend_nutripoint.services;

import com.example.backend_nutripoint.models.*;
import com.example.backend_nutripoint.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class SeedService {

    private final UsuarioRepository usuarioRepository;
    private final CategoryRepository categoryRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedService(UsuarioRepository usuarioRepository, 
                       CategoryRepository categoryRepository, 
                       MarcaRepository marcaRepository, 
                       ProductoRepository productoRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.categoryRepository = categoryRepository;
        this.marcaRepository = marcaRepository;
        this.productoRepository = productoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void seedDatabase() {
        seedUsers();
        seedMarcas();
        seedCategorias();
        seedProductos();
    }

    private void seedUsers() {
        if (!usuarioRepository.existsByEmail("admin@nutripoint.com")) {
            Usuario admin = new Usuario();
            admin.setNombres("Admin");
            admin.setApellidos("General");
            admin.setEmail("admin@nutripoint.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setDni("11111111");
            admin.setTelefono("999999991");
            admin.setRoles(List.of(Role.ADMIN));
            admin.setProvider(AuthProvider.LOCAL);
            usuarioRepository.save(admin);
        }

        if (!usuarioRepository.existsByEmail("user@nutripoint.com")) {
            Usuario user = new Usuario();
            user.setNombres("Usuario");
            user.setApellidos("Normal");
            user.setEmail("user@nutripoint.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setDni("22222222");
            user.setTelefono("999999992");
            user.setRoles(List.of(Role.USER));
            user.setProvider(AuthProvider.LOCAL);
            usuarioRepository.save(user);
        }

        if (!usuarioRepository.existsByEmail("superadmin@nutripoint.com")) {
            Usuario superadmin = new Usuario();
            superadmin.setNombres("Super");
            superadmin.setApellidos("Admin");
            superadmin.setEmail("superadmin@nutripoint.com");
            superadmin.setPassword(passwordEncoder.encode("superadmin123"));
            superadmin.setDni("33333333");
            superadmin.setTelefono("999999993");
            superadmin.setRoles(List.of(Role.SUPER_ADMIN));
            superadmin.setProvider(AuthProvider.LOCAL);
            usuarioRepository.save(superadmin);
        }
    }

    private void seedMarcas() {
        String[] marcas = {"Optimum Nutrition", "MuscleTech", "BSN", "Dymatize"};
        for (String m : marcas) {
            if (!marcaRepository.existsByNombre(m)) {
                Marca marca = new Marca();
                marca.setNombre(m);
                marcaRepository.save(marca);
            }
        }
    }

    private void seedCategorias() {
        String[][] categorias = {
            {"Proteinas", "Desarrollo muscular y recuperación"},
            {"Creatinas", "Aumento de fuerza y resistencia"},
            {"Pre-entrenos", "Energía y focus antes del entrenamiento"},
            {"Vitaminas", "Salud general e inmunidad"}
        };
        for (String[] c : categorias) {
            if (!categoryRepository.existsByCategoria(c[0])) {
                Categoria cat = new Categoria();
                cat.setCategoria(c[0]);
                cat.setObjetivo(c[1]);
                categoryRepository.save(cat);
            }
        }
    }

    private void seedProductos() {
        createProductIfNotExists("Whey Gold Standard 5lbs", "Proteína de suero de leche de la más alta calidad.", 50, "299.90", "Mezclar 1 scoop con 250ml de agua post-entrenamiento.", "No apto para intolerantes a la lactosa severos.", "Optimum Nutrition", "Proteinas");
        createProductIfNotExists("Creatina Platinum 400g", "Creatina monohidratada pura y micronizada.", 100, "89.90", "Consumir 5g diarios con agua o jugo.", "Manténgase bien hidratado durante su uso.", "MuscleTech", "Creatinas");
        createProductIfNotExists("N.O. Xplode 600g", "Pre-entreno explosivo para máxima energía y enfoque.", 30, "150.00", "Tomar 1 scoop 30 minutos antes de entrenar.", "Contiene alta dosis de cafeína, no consumir de noche.", "BSN", "Pre-entrenos");
        createProductIfNotExists("ISO100 Hydrolyzed 5lbs", "Proteína hidrolizada de rápida absorción.", 40, "320.00", "Mezclar 1 scoop con agua inmediatamente después de entrenar.", "Consultar a su médico si padece alguna enfermedad renal.", "Dymatize", "Proteinas");
        createProductIfNotExists("Syntha-6 Edge 4lbs", "Matriz de proteínas de liberación sostenida.", 45, "250.00", "Mezclar 1 scoop como reemplazo de comida o entre comidas.", "Contiene derivados de leche y soja.", "BSN", "Proteinas");
        createProductIfNotExists("Amino Energy 30 Servings", "Complejo de aminoácidos con energía natural.", 60, "110.00", "Mezclar 2 scoops con agua antes o durante el ejercicio.", "Contiene cafeína.", "Optimum Nutrition", "Pre-entrenos");
        createProductIfNotExists("Opti-Men Multivitaminico 90 tabs", "Multivitamínico completo formulado para hombres.", 80, "130.00", "Tomar 3 tabletas al día con las comidas.", "No exceder la dosis diaria recomendada.", "Optimum Nutrition", "Vitaminas");
        createProductIfNotExists("Platinum Multivitamin 90 tabs", "Vitaminas y minerales para mejorar la salud general.", 85, "120.00", "Tomar 1 servicio (3 capletas) al día con un vaso de agua.", "Para uso exclusivo de adultos.", "MuscleTech", "Vitaminas");
        createProductIfNotExists("Pre-Workout VaporX5", "Pre-entrenamiento diseñado para bombeos extremos.", 35, "140.00", "Mezclar 1 scoop con agua 30 minutos antes de entrenar.", "Evaluar su tolerancia con medio scoop al inicio.", "MuscleTech", "Pre-entrenos");
        createProductIfNotExists("Elite 100% Whey 5lbs", "Mezcla de proteína concentrada y aislada de excelente sabor.", 55, "280.00", "Añadir 1 scoop a 180-240ml de agua o leche.", "Guardar en un lugar fresco y seco.", "Dymatize", "Proteinas");
    }

    private void createProductIfNotExists(String nombre, String descripcion, Integer stock, String precio, 
                                          String modEmpleo, String advert, String marcaStr, String categoriaStr) {
        if (!productoRepository.existsByNombre(nombre)) {
            Producto p = new Producto();
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setStock(stock);
            p.setPrecioUnit(new BigDecimal(precio));
            p.setModEmpleo(modEmpleo);
            p.setAdvert(advert);
            
            Marca m = marcaRepository.findByNombre(marcaStr).orElse(null);
            p.setMarca(m);
            
            Categoria c = categoryRepository.findByCategoria(categoriaStr).orElse(null);
            if (c != null) {
                p.setCategorias(Set.of(c));
            }
            
            if(m != null) productoRepository.save(p);
        }
    }
}
