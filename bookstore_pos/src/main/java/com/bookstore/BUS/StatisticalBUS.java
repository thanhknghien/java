package com.bookstore.BUS;

/*1. Tổng doanh thu theo ngày
2. Tổng doanh thu theo nhân viên
3. Sản phẩm bán chạy nhất
4. Khách hàng có điểm thưởng cao nhất
5. Doanh thu theo danh mục sản phẩm
6. Số lượng đơn hàng theo tháng*/

import com.bookstore.model.Order;
import com.bookstore.model.OrderDetail;
import com.bookstore.model.Customer;
import com.bookstore.model.Product;
import com.bookstore.model.Category;
import com.bookstore.model.User;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import com.bookstore.dao.OrderDAO;
import com.bookstore.dao.CustomerDAO;
import com.bookstore.dao.OrderDetailDAO;
import com.bookstore.dao.ProductDAO;
import com.bookstore.dao.CategoryDAO;
import com.bookstore.dao.UserDAO;

import java.util.*;

import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticalBUS {
    private List<Product> products = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private UserDAO userDAO = new UserDAO();
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    private Map<Integer, Double> totalSpentMap = new HashMap<>();
    private Map<Integer, Double> totalRevenueMap = new HashMap<>();
    private Map<Integer, Integer> productSalesMap = new HashMap<>();
    private Map<Integer, Double> productRevenueMap = new HashMap<>();
    private Map<Integer, Integer> categorySalesMap = new HashMap<>();

    private Map<Integer, Integer> quantitySaleMap = new HashMap<>();
    private Map<Integer, Integer> quantityBuyMap = new HashMap<>();
    public StatisticalBUS() {
        try {
            products = productDAO.getAllProducts();
            orders = orderDAO.getAllOrders();
            customers = customerDAO.getAllCustomers();
            users = userDAO.getAllUsers();
            categories = categoryDAO.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        totalSpentMap = getTotalSpentMap();
        totalRevenueMap = getTotalRevenueMap();
        productSalesMap = getProductSalesMap();
        productRevenueMap = getProductRevenueMap();
        categorySalesMap = getCategorySalesMap();
    }
    // =================================================================================================================
    // Getter Map
    //tổng tiền đã chi tiêu của từng khách hàng
    public Map<Integer, Double> getTotalSpentMap() {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orders) {
            if (order.getCustomer() != null) {
                int customerId = order.getCustomer().getId();
                map.merge(customerId, order.getTotal(), Double::sum);
            }
        }
        return map;
    }
    //tổng doanh thu của từng user
    public Map<Integer, Double> getTotalRevenueMap() {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orders) {
            if (order.getEmployee() != null) {
                int userId = order.getEmployee().getId();
                map.merge(userId, order.getTotal(), Double::sum);
            }
        }
        return map;
    }
    //số lượng đã bán của từng sản phẩm
    public Map<Integer, Integer> getProductSalesMap() {
        Map<Integer, Integer> map = new HashMap<>();
    
        for (Order order : orders) {
            try {
                for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                    int productId = orderDetail.getProduct().getId();
                    int quantity = orderDetail.getQuantity();
                    
                    map.merge(productId, quantity, Integer::sum);  
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    //Doanh thu của từng sản phẩm
    public  Map<Integer, Double> getProductRevenueMap() {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orders) {
            try {
                for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                    int productId = orderDetail.getProduct().getId();
                    double price = orderDetail.getProduct().getPrice();
                    int quantity = orderDetail.getQuantity();
                    
                    map.merge(productId, price * quantity, Double::sum);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    //số lượng đã bán danh muc
    public Map<Integer, Integer> getCategorySalesMap() {
        Map<Integer, Integer> map = new HashMap<>();
    
        // Tính tổng số lượng bán theo danh mục
        for (Order order : orders) {
            try {
                for (OrderDetail detail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                    Product product = detail.getProduct();
                    int categoryId = product.getCategoryId();
                    int quantity = detail.getQuantity();
                    
                    // Cộng dồn số lượng bán cho danh mục
                    map.merge(categoryId, quantity, Integer::sum);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    // =================================================================================================================

    // Chuyển đổi sang kiểu object
    //tổng tiền đã chi tiêu của từng khách hàng
    public Object[][] processCustomersResults() {
        DecimalFormat df = new DecimalFormat("#,###");
        List<Customer> sortedCustomers = new ArrayList<>(customers);
        sortedCustomers.sort((c1, c2) -> {
            Double spent1 = totalSpentMap.getOrDefault(c1.getId(), 0.0);
            Double spent2 = totalSpentMap.getOrDefault(c2.getId(), 0.0);
            return spent2.compareTo(spent1); // Sắp xếp giảm dần
        });
    
        // Giới hạn chỉ lấy 5 khách hàng
        int size = Math.min(5, sortedCustomers.size());
        Object[][] data = new Object[size][5];
    
        // Đổ dữ liệu vào mảng kết quả
        for (int i = 0; i < size; i++) {
            Customer customer = sortedCustomers.get(i);
            data[i][0] = customer.getId();
            data[i][1] = customer.getName();
            data[i][2] = customer.getPhone();
            data[i][3] = customer.getPoints();
            data[i][4] = df.format(totalSpentMap.getOrDefault(customer.getId(), 0.0));
        }
    
        return data;
    }
    //tổng doanh thu của từng user
    public Object[][] processUsersResults() {
        DecimalFormat df = new DecimalFormat("#,###");
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort((u1, u2) -> {
            Double revenue1 = totalRevenueMap.getOrDefault(u1.getId(), 0.0);
            Double revenue2 = totalRevenueMap.getOrDefault(u2.getId(), 0.0);
            return revenue2.compareTo(revenue1); // Sắp xếp giảm dần
        });
    
        // Giới hạn chỉ lấy 5 nhân viên
        int size = Math.min(5, sortedUsers.size());
        Object[][] data = new Object[size][4];
    
        // Đổ dữ liệu vào mảng kết quả
        for (int i = 0; i < size; i++) {
            User user = sortedUsers.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getUsername();
            data[i][2] = user.getPassword();
            data[i][3] = df.format(totalRevenueMap.getOrDefault(user.getId(), 0.0));
        }
    
        return data;
    }
    //số lượng đã bán của từng sản phẩm + Doanh thu của từng sản phẩm
    public Object[][] processProductResults() {
        DecimalFormat df = new DecimalFormat("#,###");
        List<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort((p1, p2) -> {
            Integer sales1 = productSalesMap.getOrDefault(p1.getId(), 0);
            Integer sales2 = productSalesMap.getOrDefault(p2.getId(), 0);
            return sales2.compareTo(sales1); // Sắp xếp giảm dần
        });
    
        // Giới hạn chỉ lấy 5 sản phẩm
        int size = Math.min(5, sortedProducts.size());
        Object[][] data = new Object[size][6];
    
        // Đổ dữ liệu vào mảng kết quả
        for (int i = 0; i < size; i++) {
            Product product = sortedProducts.get(i);
            data[i][0] = product.getId();
            data[i][1] = product.getImagePath();
            data[i][2] = product.getName();
            data[i][3] = product.getPrice();
            data[i][4] = productSalesMap.getOrDefault(product.getId(), 0);
            data[i][5] = df.format(productRevenueMap.getOrDefault(product.getId(), 0.0));
        }
    
        return data;
    }
    //số lượng đã bán danh muc
    public Object[][] processCategorysResults() {
        List<Category> sortedCategories = new ArrayList<>(categories);
        sortedCategories.sort((c1, c2) -> {
            Integer sales1 = categorySalesMap.getOrDefault(c1.getCategoryID(), 0);
            Integer sales2 = categorySalesMap.getOrDefault(c2.getCategoryID(), 0);
            return sales2.compareTo(sales1); // Sắp xếp giảm dần
        });

        // Giới hạn chỉ lấy 5 danh mục
        int size = Math.min(5, sortedCategories.size());
        Object[][] data = new Object[size][3];

        // Đổ dữ liệu vào mảng kết quả
        for (int i = 0; i < size; i++) {
            Category category = sortedCategories.get(i);
            data[i][0] = category.getCategoryID();
            data[i][1] = category.getName();
            data[i][2] = categorySalesMap.getOrDefault(category.getCategoryID(), 0);
        }

        return data;
    }
    // =================================================================================================================
    
    // ===== Tạo dataset cho biểu đồ =====
    // Tạo dataset cho user (Lấy 5)
    public DefaultCategoryDataset createDatasetGiveUser() { 
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort((u1, u2) -> {
            Double revenue1 = totalRevenueMap.getOrDefault(u1.getId(), 0.0);
            Double revenue2 = totalRevenueMap.getOrDefault(u2.getId(), 0.0);
            return revenue2.compareTo(revenue1); // Sắp xếp giảm dần
        });
    
        // Giới hạn chỉ lấy 5 nhân viên có doanh thu cao nhất
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = Math.min(5, sortedUsers.size());
        
        for (int i = 0; i < count; i++) {
            User user = sortedUsers.get(i);
            double totalRevenue = totalRevenueMap.getOrDefault(user.getId(), 0.0);
            dataset.addValue(totalRevenue, "Doanh thu", user.getUsername());
        }
    
        return dataset;
    }
    // Tạo dataset cho khách hàng (Lấy 5)
    public DefaultCategoryDataset createDatasetGiveCustomersPoints() {
        List<Customer> sortedCustomers = new ArrayList<>(customers);
        sortedCustomers.sort((c1, c2) -> Integer.compare(c2.getPoints(), c1.getPoints())); // Sắp xếp giảm dần
    
        // Tạo dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Lấy 5 khách hàng có điểm thưởng cao nhất
        int count = Math.min(5, sortedCustomers.size());
        for (int i = 0; i < count; i++) {
            Customer customer = sortedCustomers.get(i);
            dataset.addValue(customer.getPoints(), "Điểm thưởng", customer.getName());
        }
    
        return dataset;
    }
    public DefaultCategoryDataset createDatasetGiveCustomersTotalSpent() {
        List<Customer> sortedCustomers = new ArrayList<>(customers);
        sortedCustomers.sort((c1, c2) -> {
            Double spent1 = totalSpentMap.getOrDefault(c1.getId(), 0.0);
            Double spent2 = totalSpentMap.getOrDefault(c2.getId(), 0.0);
            return spent2.compareTo(spent1); // Sắp xếp giảm dần
        });

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Lấy 5 khách hàng có tổng chi tiêu cao nhất
        int count = Math.min(5, sortedCustomers.size());
        for (int i = 0; i < count; i++) {
            Customer customer = sortedCustomers.get(i);
            double totalSpent = totalSpentMap.getOrDefault(customer.getId(), 0.0);
            dataset.addValue(totalSpent, "Tổng chi", customer.getName());
        }
    
        return dataset;
    }
    // Tạo dataset cho sản phẩm (Lấy 5)
    public DefaultCategoryDataset createDatasetGiveProductsQuantity() {
        List<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort((p1, p2) -> {
            Integer sales1 = productSalesMap.getOrDefault(p1.getId(), 0);
            Integer sales2 = productSalesMap.getOrDefault(p2.getId(), 0);
            return sales2.compareTo(sales1); // Sắp xếp giảm dần
        });
    
        // Tạo dataset và thêm 5 sản phẩm bán chạy nhất
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = Math.min(5, sortedProducts.size());
        
        for (int i = 0; i < count; i++) {
            Product product = sortedProducts.get(i);
            int totalSales = productSalesMap.getOrDefault(product.getId(), 0);
            dataset.addValue(totalSales, "Số lượng bán", product.getName());
        }
    
        return dataset;
    }  
    public DefaultCategoryDataset createDatasetGiveProductsRevenue() {
        List<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort((p1, p2) -> {
            Double revenue1 = productRevenueMap.getOrDefault(p1.getId(), 0.0);
            Double revenue2 = productRevenueMap.getOrDefault(p2.getId(), 0.0);
            return revenue2.compareTo(revenue1); // Sắp xếp giảm dần
        });
    
        // Tạo dataset và thêm 5 sản phẩm có doanh thu cao nhất
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int count = Math.min(5, sortedProducts.size());
        
        for (int i = 0; i < count; i++) {
            Product product = sortedProducts.get(i);
            double totalRevenue = productRevenueMap.getOrDefault(product.getId(), 0.0);
            dataset.addValue(totalRevenue, "Doanh thu", product.getName());
        }
    
        return dataset;
    }
    // Tạo dataset cho danh mục sản phẩm (Lấy 5)
    public DefaultCategoryDataset createDatasetGiveCategory() {
        List<Category> sortedCategories = new ArrayList<>(categories);
        sortedCategories.sort((c1, c2) -> {
            Integer sales1 = categorySalesMap.getOrDefault(c1.getCategoryID(), 0);
            Integer sales2 = categorySalesMap.getOrDefault(c2.getCategoryID(), 0);
            return sales2.compareTo(sales1); // Sắp xếp giảm dần
        });

        // Tạo dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Thêm dữ liệu vào dataset
        int count = Math.min(5, categories.size());
        for (int i = 0; i < count; i++) {
            Category category = sortedCategories.get(i);
            int totalSales = categorySalesMap.getOrDefault(category.getCategoryID(), 0);
            dataset.addValue(totalSales, "Số lượng bán", category.getName());
        }
    
        return dataset;
    }
    // =================================================================================================================

    // Tìm kiếm
        // Top
    // Khách hàng tổng chi
    public void searchTotalSpentMap(LocalDateTime frDate, LocalDateTime toDate) {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orders) {
            if (order.getCustomer() != null && 
                (order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) && 
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                
                int customerId = order.getCustomer().getId();
                map.merge(customerId, order.getTotal(), Double::sum);
            }
        }
        totalSpentMap = map;
    }
    // tổng doanh thu của từng user
    public void searchTotalRevenueMap(LocalDateTime frDate, LocalDateTime toDate) {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orders) {
            if (order.getEmployee() != null && 
                (order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) && 
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                
                int userId = order.getEmployee().getId();
                map.merge(userId, order.getTotal(), Double::sum);
            }
        }
        totalRevenueMap = map;
    }
    //số lượng đã bán của từng sản phẩm
    public void searchProductSalesMap(LocalDateTime frDate, LocalDateTime toDate) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Order order : orders) {
            if ((order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) &&
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                try {
                    for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                        int productId = orderDetail.getProduct().getId();
                        int quantity = orderDetail.getQuantity();
                        
                        map.merge(productId, quantity, Integer::sum);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        productSalesMap = map;
    }
    //Doanh thu của từng sản phẩm
    public void searchProductRevenueMap(LocalDateTime frDate, LocalDateTime toDate) {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orders) {
            if ((order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) &&
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                try {
                    for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                        int productId = orderDetail.getProduct().getId();
                        double price = orderDetail.getProduct().getPrice();
                        int quantity = orderDetail.getQuantity();
                        
                        map.merge(productId, price * quantity, Double::sum);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        productRevenueMap = map;
    }
    //số lượng đã bán danh muc
    public void searchCategorySalesMap(LocalDateTime frDate, LocalDateTime toDate) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Order order : orders) {
            if ((order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) &&
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                try {
                    for (OrderDetail detail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                        Product product = detail.getProduct();
                        int categoryId = product.getCategoryId();
                        int quantity = detail.getQuantity();

                        map.merge(categoryId, quantity, Integer::sum);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        categorySalesMap = map;
    }


    // All
    public Object[][] searchProduct(LocalDateTime frDate, LocalDateTime toDate, String keyword) {
        int quantity = 0;
        // Lọc các hóa đơn vừa tìm đc theo ngày
        for (Order order : orders) {
            if ((order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) && 
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                try {
                    for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                        if (String.valueOf(orderDetail.getProduct().getId()).equals(keyword)) {
                            quantity += orderDetail.getQuantity();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }                
            }
        }
        
        String name = "";
        Double price = 0.0;
        for (Product pr : products) {
            if (String.valueOf(pr.getId()).equals(keyword)) {
                name = pr.getName();
                price = pr.getPrice();
            }
        }
        DecimalFormat df = new DecimalFormat("#,###");
        Object[][] data = new Object[1][5];
        data[0][0] = keyword;
        data[0][1] = name;
        data[0][2] = df.format(price);
        data[0][3] = String.valueOf(quantity);
        data[0][4] = df.format(price*quantity);
        return data;

    }

    public Object[][] searchCustomer(LocalDateTime frDate, LocalDateTime toDate, String keyword) {
        List<Order> filter = new ArrayList<>();

        // Tìm các hóa đơn keyword
        for (Order order : orders) {
            if (String.valueOf(order.getCustomer().getId()).equals(keyword)) {
                filter.add(order);
            }
        }
        

        // Lọc các hóa đơn vừa tìm đc theo ngày
        for (Order order : filter) {
            if ((order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) && 
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                try {
                    for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                        quantityBuyMap.merge(orderDetail.getProduct().getId(), orderDetail.getQuantity(), Integer::sum);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }                
            }
        }

        Object[][] data = new Object[quantityBuyMap.size()][2];

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : quantityBuyMap.entrySet()) {
            data[index][0] = entry.getKey(); // Product ID
            data[index][1] = entry.getValue(); // Quantity Sold
            index++;
        }
        return data;
    }

    public Object[][] searchUser(LocalDateTime frDate, LocalDateTime toDate, String keyword) {
        List<Order> filter = new ArrayList<>();

        // Tìm các hóa đơn keyword
        for (Order order : orders) {
            if (String.valueOf(order.getEmployee().getId()).equals(keyword)) {
                filter.add(order);
            }
        }

        // Lọc các hóa đơn vừa tìm đc theo ngày
        for (Order order : filter) {
            if ((order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate)) && 
                (order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))) {
                try {
                    for (OrderDetail orderDetail : orderDetailDAO.getOrderDetailsByOrderId(order.getId())) {
                        quantitySaleMap.merge(orderDetail.getProduct().getId(), orderDetail.getQuantity(), Integer::sum);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }                
            }
        }

        Object[][] data = new Object[quantitySaleMap.size()][2];

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : quantitySaleMap.entrySet()) {
            data[index][0] = entry.getKey(); // Product ID
            data[index][1] = entry.getValue(); // Quantity Sold
            index++;
        }
        for (Object[] row : data) {
            System.out.println(Arrays.toString(row));
        }
        return data;
    }

    public String totalSpent() {
        DecimalFormat df = new DecimalFormat("#,###");
        Double total = 0.0;
        for (Product pr : products) {
            for (Map.Entry<Integer, Integer> entry : quantityBuyMap.entrySet()){
                if (Integer.valueOf(pr.getId()).equals(entry.getKey())){
                    total += (pr.getPrice()*entry.getValue());
                }
            }
        }
        return df.format(total);
    }

    public String totalRevenue() {
        DecimalFormat df = new DecimalFormat("#,###");
        Double total = 0.0;
        for (Product pr : products) {
            for (Map.Entry<Integer, Integer> entry : quantitySaleMap.entrySet()){
                if (Integer.valueOf(pr.getId()).equals(entry.getKey())){
                    total += (pr.getPrice()*entry.getValue());
                }
            }
        }
        return df.format(total);
    }

    public String searchCustomer(String keyword) {
        String str= keyword;
        for (Customer cus : customers) {
            if (String.valueOf(cus.getId()).equals(keyword)) {
                str += " "+  cus.getName() + "\n" + cus.getPhone();
            }
        }
        return str;
    }

    public String getQuantityCustomer() {
        return String.valueOf(customers.size());
    }

    public String getQuantityProduct() {
        return String.valueOf(products.size());
    }

    public String getQuantityOrder() {
        return String.valueOf(orders.size());
    }
}
