package com.bookstore.BUS;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private Map<Integer, Integer> quantitySaleMap;
    private Map<Integer, Integer> quantityBuyMap;
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
        return orders.stream()
            .filter(order -> order.getCustomer() != null)
            .collect(Collectors.groupingBy(
                order -> order.getCustomer().getId(),
                Collectors.summingDouble(Order::getTotal)
            ));
    }
    //tổng doanh thu của từng user
    public Map<Integer, Double> getTotalRevenueMap() {
        return orders.stream()
            .filter(order -> order.getEmployee() != null)
            .collect(Collectors.groupingBy(
                order -> order.getEmployee().getId(),
                Collectors.summingDouble(Order::getTotal)
            ));
    }
    //số lượng đã bán của từng sản phẩm
    public Map<Integer, Integer> getProductSalesMap() {
        return orders.stream()
            .flatMap(order -> {
                try {
                    return orderDetailDAO.getOrderDetailsByOrderId(order.getId()).stream();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return Stream.empty();
                }
            })
            .collect(Collectors.groupingBy(
                detail -> detail.getProduct().getId(),
                Collectors.summingInt(OrderDetail::getQuantity)
            ));
    }
    //Doanh thu của từng sản phẩm
    public Map<Integer, Double> getProductRevenueMap() {
        return orders.stream()
            .flatMap(order -> {
                try {
                    return orderDetailDAO.getOrderDetailsByOrderId(order.getId()).stream();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return Stream.empty();
                }
            })
            .collect(Collectors.groupingBy(
                detail -> detail.getProduct().getId(),
                Collectors.summingDouble(detail -> detail.getProduct().getPrice() * detail.getQuantity())
            ));
    }
    //số lượng đã bán danh muc
    public Map<Integer, Integer> getCategorySalesMap() {
        return orders.stream()
            .flatMap(order -> {
                try {
                    return orderDetailDAO.getOrderDetailsByOrderId(order.getId()).stream();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return Stream.empty();
                }
            })
            .collect(Collectors.groupingBy(
                detail -> detail.getProduct().getCategoryId(),
                Collectors.summingInt(OrderDetail::getQuantity)
            ));
    }
    // =================================================================================================================

    // Chuyển đổi sang kiểu object
    //tổng tiền đã chi tiêu của từng khách hàng
    public Object[][] processCustomersResults() {
        DecimalFormat df = new DecimalFormat("#,###");
        return customers.stream()
            .sorted((c1, c2) -> {
                Double spent1 = totalSpentMap.getOrDefault(c1.getId(), 0.0);
                Double spent2 = totalSpentMap.getOrDefault(c2.getId(), 0.0);
                return spent2.compareTo(spent1);
            })
            .limit(5)
            .map(customer -> new Object[]{
                customer.getId(),
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getPoints(),
                df.format(totalSpentMap.getOrDefault(customer.getId(), 0.0))
            })
            .toArray(Object[][]::new);
    }
    //tổng doanh thu của từng user
    public Object[][] processUsersResults() {
        DecimalFormat df = new DecimalFormat("#,###");
        return users.stream()
            .sorted((u1, u2) -> {
                Double revenue1 = totalRevenueMap.getOrDefault(u1.getId(), 0.0);
                Double revenue2 = totalRevenueMap.getOrDefault(u2.getId(), 0.0);
                return revenue2.compareTo(revenue1);
            })
            .limit(5)
            .map(user -> new Object[]{
                user.getId(),
                user.getUsername(),
                df.format(totalRevenueMap.getOrDefault(user.getId(), 0.0))
            })
            .toArray(Object[][]::new);
    }
    //số lượng đã bán của từng sản phẩm + Doanh thu của từng sản phẩm
    public Object[][] processProductResults() {
        DecimalFormat df = new DecimalFormat("#,###");
        return products.stream()
            .sorted((p1, p2) -> {
                Integer sales1 = productSalesMap.getOrDefault(p1.getId(), 0);
                Integer sales2 = productSalesMap.getOrDefault(p2.getId(), 0);
                return sales2.compareTo(sales1);
            })
            .limit(5)
            .map(product -> new Object[]{
                product.getId(),
                product.getImagePath(),
                product.getName(),
                product.getPrice(),
                productSalesMap.getOrDefault(product.getId(), 0),
                df.format(productRevenueMap.getOrDefault(product.getId(), 0.0))
            })
            .toArray(Object[][]::new);
    }
    //số lượng đã bán danh muc
    public Object[][] processCategorysResults() {
        return categories.stream()
            .sorted((c1, c2) -> {
                Integer sales1 = categorySalesMap.getOrDefault(c1.getCategoryID(), 0);
                Integer sales2 = categorySalesMap.getOrDefault(c2.getCategoryID(), 0);
                return sales2.compareTo(sales1);
            })
            .limit(5)
            .map(category -> new Object[]{
                category.getCategoryID(),
                category.getName(),
                categorySalesMap.getOrDefault(category.getCategoryID(), 0)
            })
            .toArray(Object[][]::new);
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
            dataset.addValue(customer.getPoints(), "Điểm thưởng", customer.getFullName());
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
            dataset.addValue(totalSpent, "Tổng chi", customer.getFullName());
        }
    
        return dataset;
    }
    // Tạo dataset cho sản phẩm (Lấy 5)
    public DefaultCategoryDataset createDatasetGiveProductsQuantity() {
        return products.stream()
            .sorted((p1, p2) -> {
                Integer sales1 = productSalesMap.getOrDefault(p1.getId(), 0);
                Integer sales2 = productSalesMap.getOrDefault(p2.getId(), 0);
                return sales2.compareTo(sales1);
            })
            .limit(5)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    list.forEach(product -> 
                        dataset.addValue(
                            productSalesMap.getOrDefault(product.getId(), 0),
                            "Số lượng bán",
                            product.getName()
                        )
                    );
                    return dataset;
                }
            ));
    }
    public DefaultCategoryDataset createDatasetGiveProductsRevenue() {
        return products.stream()
            .sorted((p1, p2) -> {
                Double revenue1 = productRevenueMap.getOrDefault(p1.getId(), 0.0);
                Double revenue2 = productRevenueMap.getOrDefault(p2.getId(), 0.0);
                return revenue2.compareTo(revenue1);
            })
            .limit(5)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    list.forEach(product -> 
                        dataset.addValue(
                            productRevenueMap.getOrDefault(product.getId(), 0.0),
                            "Doanh thu",
                            product.getName()
                        )
                    );
                    return dataset;
                }
            ));
    }
    // Tạo dataset cho danh mục sản phẩm (Lấy 5)
    public DefaultCategoryDataset createDatasetGiveCategory() {
        return categories.stream()
            .sorted((c1, c2) -> {
                Integer sales1 = categorySalesMap.getOrDefault(c1.getCategoryID(), 0);
                Integer sales2 = categorySalesMap.getOrDefault(c2.getCategoryID(), 0);
                return sales2.compareTo(sales1);
            })
            .limit(5)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    list.forEach(category -> 
                        dataset.addValue(
                            categorySalesMap.getOrDefault(category.getCategoryID(), 0),
                            "Số lượng bán",
                            category.getName()
                        )
                    );
                    return dataset;
                }
            ));
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
        productSalesMap = orders.parallelStream()
        .filter(order -> order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate))
        .filter(order -> order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))
        .flatMap(order -> {
            try {
                return orderDetailDAO.getOrderDetailsByOrderId(order.getId()).stream();
            } catch (SQLException e) {
                return Stream.empty();
            }
        })
        .collect(Collectors.groupingBy(
            detail -> detail.getProduct().getId(),
            Collectors.summingInt(OrderDetail::getQuantity)
        ));
    }
    //Doanh thu của từng sản phẩm
    public void searchProductRevenueMap(LocalDateTime frDate, LocalDateTime toDate) {
        productRevenueMap = orders.parallelStream()
        .filter(order -> order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate))
        .filter(order -> order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))
        .flatMap(order -> {
            try {
                return orderDetailDAO.getOrderDetailsByOrderId(order.getId()).stream();
            } catch (SQLException e) {
                return Stream.empty();
            }
        })
        .collect(Collectors.groupingBy(
            detail -> detail.getProduct().getId(),
            Collectors.summingDouble(detail -> detail.getProduct().getPrice() * detail.getQuantity())
        ));
    }
    //số lượng đã bán danh muc
    public void searchCategorySalesMap(LocalDateTime frDate, LocalDateTime toDate) {
        categorySalesMap = orders.parallelStream()
        .filter(order -> order.getDate().isAfter(frDate) || order.getDate().isEqual(frDate))
        .filter(order -> order.getDate().isBefore(toDate) || order.getDate().isEqual(toDate))
        .flatMap(order -> {
            try {
                return orderDetailDAO.getOrderDetailsByOrderId(order.getId()).stream();
            } catch (SQLException e) {
                return Stream.empty();
            }
        })
        .collect(Collectors.groupingBy(
            detail -> detail.getProduct().getCategoryId(),
            Collectors.summingInt(OrderDetail::getQuantity)
        ));
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
        
        String category_name = "";
        String name = "";
        Double price = 0.0;
        for (Product pr : products) {
            if (String.valueOf(pr.getId()).equals(keyword)) {
                name = pr.getName();
                category_name = categories.stream()
                    .filter(c -> c.getCategoryID() == pr.getCategoryId())
                    .map(Category::getName)
                    .findFirst()
                    .orElse("Unknown");
                price = pr.getPrice();
            }
        }
        DecimalFormat df = new DecimalFormat("#,###");
        Object[][] data = new Object[1][6];
        data[0][0] = keyword;
        data[0][1] = name;
        data[0][2] = category_name;
        data[0][3] = df.format(price);
        data[0][4] = String.valueOf(quantity);
        data[0][5] = df.format(price*quantity);
        System.out.println(Arrays.deepToString(data));
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
        
        quantityBuyMap = new HashMap<>();
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

        Object[][] data = new Object[quantityBuyMap.size()][5];

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : quantityBuyMap.entrySet()) {
            data[index][0] = entry.getKey(); // Product ID
            for (Product product : products) {
                if (product.getId() == entry.getKey()) {
                    data[index][1] = product.getName(); // Product Name
                    data[index][2] = product.getPrice(); // Price
                    break;
                }
            }
            data[index][3] = entry.getValue(); // Quantity Sold
            data[index][4] = entry.getValue() * ((Double) data[index][2]); // Total
            index++;
        }
        for (Object[] row : data) {
            System.out.println(Arrays.toString(row));
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
        quantitySaleMap = new HashMap<>();
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

        Object[][] data = new Object[quantitySaleMap.size()][5];

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : quantitySaleMap.entrySet()) {
            data[index][0] = entry.getKey(); // Product ID
            for (Product product : products) {
                if (product.getId() == entry.getKey()) {
                    data[index][1] = product.getName(); // Product Name
                    data[index][2] = product.getPrice(); // Price
                    break;
                }
            }
            data[index][3] = entry.getValue(); // Quantity Sold
            data[index][4] = entry.getValue() * ((Double) data[index][2]); // Total
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

    public ArrayList<String> searchCustomer(String keyword) {
        ArrayList<String> result = new ArrayList<>();
        for (Customer cus : customers) {
            if (String.valueOf(cus.getId()).equals(keyword)) { 
                result.add(String.valueOf(cus.getId()));    // id
                result.add(cus.getFullName());              // ten
                result.add(cus.getPhoneNumber());           // sdt
                result.add(String.valueOf(cus.getPoints()));// diem
                return result;
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<String> searchProduct(String keyword) {
        ArrayList<String> result = new ArrayList<>();
        for (Product pr : products) {
            if (String.valueOf(pr.getId()).equals(keyword)) {
                result.add(String.valueOf(pr.getId()));
                result.add(pr.getName());
                result.add(String.valueOf(pr.getPrice()));
                return result;
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<String> searchUser(String keyword) {
        ArrayList<String> result = new ArrayList<>();
        for (User us : users) {
            if (String.valueOf(us.getId()).equals(keyword)) {
                result.add(String.valueOf(us.getId()));     // id
                result.add(us.getUsername());               // name
                return result;
            }
        }
        return new ArrayList<>();
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

    public ArrayList<String> searchProduct(Object[][] data) {
        ArrayList<String> result = new ArrayList<>();
        result.add(String.valueOf(data[0][0])); // id
        result.add(String.valueOf(data[0][1])); // ten
        result.add(String.valueOf(data[0][2])); // danh muc
        result.add(String.valueOf(data[0][3])); // gia
        result.add(String.valueOf(data[0][4])); // so luong
        result.add(String.valueOf(data[0][5])); // doanh thu
        return result;
    }
}
