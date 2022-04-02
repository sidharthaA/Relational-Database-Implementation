package edu.rit.ibd.a4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

public class Transactions_729 {
	static int countNegative = 0;
	static Connection con;	
	
	public static void main(String[] args) throws Exception {
		// establishing connection
		final String dbURL = "jdbc:mysql://localhost:3306/UsersProducts" ;
		final String user =  "root";
		final String pwd = "root";

		con = DriverManager.getConnection(dbURL, user, pwd);
		con.setTransactionIsolation(con.TRANSACTION_SERIALIZABLE);
		System.out.println("main started");
        //initialization
		Populate(); 
	}
	
	public static void Populate() throws SQLException {
		
		Statement stmt = con.createStatement();
		
        String query = "DELETE FROM user";
        stmt.executeUpdate(query);
        query = "DELETE FROM product";
        stmt.executeUpdate(query);
        query = "DELETE FROM review";
        stmt.executeUpdate(query);
        query = "DELETE FROM purchase";
        stmt.executeUpdate(query);
	        
		//creating users
        for(int i = 1; i <=1000; i++) {
        	CreateAccount("user" + i, "pwd" + i, "firstName" + i, "lastName" + i);
        }
        
        // Creating products
        for(int i = 1; i <=10000; i++) {
        	Random randStock = new Random();
        	Random randPrice = new Random();
            int ubStock = 101;
            int ubPrice = 1001;
            int stock = randStock.nextInt(ubStock) + 1;
            int price = randPrice.nextInt(ubPrice) + 1;
        	AddProduct("prod" + i, "desc" + i, price, stock);
        }
        
        // Creating reviews
        for(int i = 1; i <=20000; i++) {
        	Random randUser = new Random();
        	Random randProdID = new Random();
        	Random randRating = new Random();
            int ubUser = 1001;
            int ubProdID = 10001;
            int ubRating = 10;
            int j = randUser.nextInt(ubUser) + 1;
            int prodID = randProdID.nextInt(ubProdID) + 1;
            int rating = randRating.nextInt(ubRating) + 1;
            try {
            PostReview("user" + j, "pwd" + j, prodID, rating, "reviewText" + i);
            }
            catch(Exception e) {
            	
            }
        }
        
        //Submit order
        for(int i = 1; i <=10000; i++) {
        	Random randUser = new Random();
        	Random randProdID = new Random();
	    	int ubUser = 1001;
	        int ubProdID = 10001;
        	int j = randUser.nextInt(ubUser) + 1;
        	Random randQuantity = new Random();
        	Random randMonth = new Random();
        	Random randDay = new Random();
        	int month = randMonth.nextInt(12) + 1;
        	int day = randDay.nextInt(28) + 1;
        	String date = 2020 + "-" + month + "-" + day;
        	HashMap<Integer, Integer> listOfProductsAndQuantities = new HashMap<Integer, Integer>();
        	for(int x = 1; x <= 10; x++) {
        		int prodID = randProdID.nextInt(ubProdID) + 1;
        		int quantity = randQuantity.nextInt(20) + 1;
        		listOfProductsAndQuantities.put(prodID, quantity);
        	}
	        SubmitOrder(date, "user" + j, "pwd" + j, listOfProductsAndQuantities);
        }
        System.out.println("populated!");
	}
		
	//CreateAccount
	public static void CreateAccount (String username, String password,String firstName, String lastName) throws SQLException {
		String sql = "INSERT INTO user VALUES ('" + username + "','" + password + "','" + firstName + "','" + lastName + "')";
		PreparedStatement st = con.prepareStatement("SELECT * FROM user WHERE User_Name = '" + username + "'");
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
        	
        } else {
        	st = con.prepareStatement(sql);
			st.execute();
        }
	}
	
	//AddProduct
	public static void AddProduct(String name, String description, int price, int initialStock) throws SQLException {
		Statement stmt = con.createStatement();
		String query = "SELECT COUNT(*) FROM product";
		ResultSet rs = stmt.executeQuery(query);
		int count = 0;
		while(rs.next()){
		    count = rs.getInt("COUNT(*)");				
		  }
		int prodID = count + 1;
		String sql = "INSERT INTO product VALUES (" + prodID + ",'" + name + "','" + description +"'," + price +"," + initialStock +")";
		stmt.executeUpdate(sql);
	}
	
	//SubmitOrder
	public static void SubmitOrder(String date, String username, String password, HashMap<Integer, Integer> listOfProductsAndQuantities) throws SQLException {
		// check whether given user name exists and if password matches
		PreparedStatement st = con.prepareStatement("SELECT Password FROM user WHERE User_Name = '" + username + "'");
		ResultSet rs = st.executeQuery();
		String userPWD = "";
		// checking if user name exists
		if(rs.next()) {
			// checking if passwords match
            	userPWD = rs.getString("Password");
        	if (userPWD.equals(password)) {
        		// creating order ID
    			Statement stmt = con.createStatement();
    			String query = "SELECT COUNT(*) FROM purchase";
    			ResultSet rss = stmt.executeQuery(query);
    			int count = 0;
    			while(rss.next()){
    			    count = rss.getInt(1);				
    			}
    			int orderID = count + 1;
    			for (int prodID : listOfProductsAndQuantities.keySet()) {
					PreparedStatement st2 = con.prepareStatement("SELECT Count_Stock FROM product WHERE Product_ID = " + prodID);
					ResultSet rs3 = st2.executeQuery();
					int qty = 0;
	    			if(rs3.next()){
	    			    qty = rs3.getInt("Count_Stock");
	    			    if(qty >= listOfProductsAndQuantities.get(prodID)) {
	    			    	String sql = "INSERT INTO purchase VALUES (" + orderID + "," + prodID + "," + qty +",'" + username +"','" + date +"')";
	    			    	PreparedStatement ps = con.prepareStatement(sql);
	    			    	ps.executeUpdate(sql);
	    					int totalCount = qty - listOfProductsAndQuantities.get(prodID);
	    			    	sql = "UPDATE product SET Count_Stock = " + totalCount + " WHERE Product_ID = " + prodID;
	    			    	ps = con.prepareStatement(sql);
	    			    	ps.executeUpdate(sql);
	    			    	PreparedStatement sttt = con.prepareStatement("SELECT Count_Stock FROM product WHERE Product_ID = " + prodID);
	    					ResultSet rss3 = sttt.executeQuery();
	    					if(rs3.next()){
	    	    			    int countt = rss3.getInt("Count_Stock");
	    	    			    if(countt < 0) {
	    	    			    	countNegative++;
	    	    			    }
	    					}
	    			    }
	    			}
    			}		
            }
        }
	}
	
	//PostReview
	public static void PostReview(String username, String password, int productID, int rating, String reviewText) throws SQLException {
		// check whether given user name exists and if password matches
		PreparedStatement st = con.prepareStatement("SELECT User_Name, Password FROM user WHERE User_Name = '" + username + "'");
		ResultSet rs = st.executeQuery();
		String userPWD = "";
		// checking if user name exists
		if(rs.next()) {
			// checking if passwords match
            	userPWD = rs.getString("Password");
        }
    	if (userPWD.equals(password)) {
    		// checking if user has already posted a review about the same product
    		PreparedStatement st2 = con.prepareStatement("SELECT Product_ID FROM review WHERE User_ID = '" + username + "' and Product_id = '"+productID+"'");
    		ResultSet rs2 = st2.executeQuery();
    		String prodID = "";
    		if (rs2.next()) {
    			prodID = rs2.getString("Product_ID");// no review
            }else {
            	Random randMonth = new Random();
            	Random randDay = new Random();
            	int month = randMonth.nextInt(12) + 1;
            	int day = randDay.nextInt(28) + 1;
            	String date = 2020 + "-" + month + "-" + day;
    			String sql = "INSERT INTO review VALUES ('" + username + "'," + productID + "," + rating +",'" + reviewText + "', '" + date + "')";
    			try {
        			st2.executeUpdate(sql);
    			}
    			catch(Exception e) {
    				
    			}
            }
    	}
	}
	
	//UpdateStockLevel
	public static void UpdateStockLevel(int productID, int itemCountToAdd) throws Exception {
		PreparedStatement st = con.prepareStatement("SELECT Count_Stock FROM product WHERE Product_ID = " + productID);
		ResultSet rs = st.executeQuery();
		int count = 0;
		while(rs.next()){
		    count = rs.getInt("Count_Stock");
		}
		int totalCount = count + itemCountToAdd;
		String sq = "UPDATE product SET Count_Stock = " + totalCount + " WHERE Product_ID = " + productID;
		PreparedStatement ps = con.prepareStatement(sq);
		ps.execute(sq);
	}
	
	//GetProductAndReviews
	public static void GetProductAndReviews(int productID) throws SQLException {
		PreparedStatement st = con.prepareStatement("SELECT product.Product_ID, product.Name, product.Description, product.Price,"
				+ " product.Count_Stock, review.User_ID, review.Rating, review.Review FROM product "
				+ "JOIN review ON review.Product_ID = product.Product_ID WHERE product.Product_ID = " + productID);
		st.execute();
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
//			System.out.println(rs.getInt(1));
//			System.out.println(rs.getString(2));
//			System.out.println(rs.getString(3));
//			System.out.println(rs.getInt(4));
//			System.out.println(rs.getInt(5));
//			System.out.println(rs.getString(6));
//			System.out.println(rs.getInt(7));
//			System.out.println(rs.getString(8));
		}
	}
	
	//GetAverageUserRating
	public static void GetAverageUserRating(String username) throws SQLException {
		PreparedStatement st = con.prepareStatement("SELECT AVG(Rating) FROM review WHERE User_ID = '" + username + "'");
		st.execute();
		ResultSet rs = st.executeQuery();
		if (rs.next()) {
//			System.out.println(rs.getInt(1));
		}
	}
}
