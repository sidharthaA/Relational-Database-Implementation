package edu.rit.ibd.a4;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class threadsTransaction extends Thread {
	
	public void run(){
		Random randProbability = new Random();
    	int probability = randProbability.nextInt(101);
    	probability /= 100; 
    	
    	//CreateAccount
    	if (probability > 0 && probability <= 0.03) {
    		Random randUser = new Random();
        	int i = randUser.nextInt(10000) + 1001;
			try {
				Transactions_729.CreateAccount("user" + i, "pwd" + i, "firstName" + i, "lastName" + i);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
    	}
    	
    	// AddProduct
    	if (probability > 0.03 && probability <= 0.05) {
    		Random randStock = new Random();
        	Random randPrice = new Random();
        	Random randProdID = new Random();
            int stock = randStock.nextInt(101) + 1;
            int price = randPrice.nextInt(1001) + 1;  
            int i = randProdID.nextInt(10000) + 10001;
			try {
				Transactions_729.AddProduct("prod" + i, "desc" + i, price, stock);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
    	}
    	
    	//UpdateStockLevel
    	if (probability > 0.05 && probability <= 0.15) {
    		Random randCountToAdd = new Random();
        	Random randProdID = new Random();
            int countToAdd = randCountToAdd.nextInt(10) + 1;
            int prodID = randProdID.nextInt(10000) + 1;
            try {
				Transactions_729.UpdateStockLevel(prodID, countToAdd);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//GetProductAndReviews
    	if (probability > 0.15 && probability <= 0.8) {
    		Random randProdID = new Random();
    		int prodID = randProdID.nextInt(10000) + 1;
    		try {
				Transactions_729.GetProductAndReviews(prodID);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//GetAverageUserRating
    	if (probability > 0.8 && probability <= 0.85) {
    		Random randUser = new Random();
        	int i = randUser.nextInt(1001) + 1;
    		try {
				Transactions_729.GetAverageUserRating("user" + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//SubmitOrder
    	if (probability > 0.85 && probability <= 0.95) {
    		Random randUser = new Random();
        	Random randProdID = new Random();
        	Random randQuantity = new Random();
        	Random randMonth = new Random();
        	Random randDay = new Random();
	    	int ubUser = 1001;
	        int ubProdID = 10001;
        	int j = randUser.nextInt(ubUser) + 1;
        	int month = randMonth.nextInt(12) + 1;
        	int day = randDay.nextInt(28) + 1;
        	String date = 2020 + "-" + month + "-" + day;
        	HashMap<Integer, Integer> listOfProductsAndQuantities = new HashMap<Integer, Integer>();
        	for(int x = 1; x <= 10; x++) {
        		int prodID = randProdID.nextInt(ubProdID) + 1;
        		int quantity = randQuantity.nextInt(20) + 1;
        		listOfProductsAndQuantities.put(prodID, quantity);
        	}
    		try {
				Transactions_729.SubmitOrder(date, "user" + j, "pwd" + j, listOfProductsAndQuantities);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	//PostReview
    	if (probability > 0.95 && probability <= 1) {
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
				Transactions_729.PostReview("user" + j, "pwd" + j, prodID, rating, "reviewText" + j);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
	}
	
	public static void main(String[] args) throws Exception{
		
		//populate function call
		Transactions_729.main(args);
		int counter = 0;
		//threads count 1
		long startTime1 = System.currentTimeMillis();
		System.out.println("thread1");
		
	    while (System.currentTimeMillis() < startTime1 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	t1.start();
	    	t1.join();
	    	counter++;
	    }
	    
	    FileWriter myWriter = new FileWriter("record.txt");
	      myWriter.write("1 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("1 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("thread1 ends");
	    
	    Transactions_729.Populate();
	  //threads count 2
	    counter = 0;
	    long startTime2 = System.currentTimeMillis();
	    System.out.println("thread2");
	    while (System.currentTimeMillis() < startTime2 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t1.join();
	    	t2.join();
	    	counter++;
	    }
	    
	    myWriter.write("2 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("2 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("thread2 ends");
	    
	    Transactions_729.Populate();
	  //threads count 3
	    counter = 0;
	    long startTime3 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime3 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	counter++;
	    }
	    
	    myWriter.write("3 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("3 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 4
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime4 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime4 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	counter++;
	    }
	    
	    myWriter.write("4 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("4 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 5
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime5 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime5 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	Thread t5 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	counter++;
	    }
	    
	    myWriter.write("5 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("5 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 6
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime6 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime6 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	Thread t5 = new threadsTransaction();
	    	Thread t6 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	counter++;
	    }
	    
	    myWriter.write("6 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("6 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 7
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime7 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime7 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	Thread t5 = new threadsTransaction();
	    	Thread t6 = new threadsTransaction();
	    	Thread t7 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	counter++;
	    }
	    
	    myWriter.write("7 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("7 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 8
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime8 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime8 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	Thread t5 = new threadsTransaction();
	    	Thread t6 = new threadsTransaction();
	    	Thread t7 = new threadsTransaction();
	    	Thread t8 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t8.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	t8.join();
	    	counter++;
	    }
	    
	  //threads count 9
	    myWriter.write("8 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("8 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime9 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime9 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	Thread t5 = new threadsTransaction();
	    	Thread t6 = new threadsTransaction();
	    	Thread t7 = new threadsTransaction();
	    	Thread t8 = new threadsTransaction();
	    	Thread t9 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t8.start();
	    	t9.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	t8.join();
	    	t9.join();
	    	counter++;
	    }
	    
	    myWriter.write("9 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("9 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    
	  //threads count 10
	    Transactions_729.Populate();
	    counter = 0;
	    long startTime10 = System.currentTimeMillis();
	    while (System.currentTimeMillis() < startTime10 + 300000) {
	    	Thread t1 = new threadsTransaction();
	    	Thread t2 = new threadsTransaction();
	    	Thread t3 = new threadsTransaction();
	    	Thread t4 = new threadsTransaction();
	    	Thread t5 = new threadsTransaction();
	    	Thread t6 = new threadsTransaction();
	    	Thread t7 = new threadsTransaction();
	    	Thread t8 = new threadsTransaction();
	    	Thread t9 = new threadsTransaction();
	    	Thread t10 = new threadsTransaction();
	    	t1.start();
	    	t2.start();
	    	t3.start();
	    	t4.start();
	    	t5.start();
	    	t6.start();
	    	t7.start();
	    	t8.start();
	    	t9.start();
	    	t10.start();
	    	t1.join();
	    	t2.join();
	    	t3.join();
	    	t4.join();
	    	t5.join();
	    	t6.join();
	    	t7.join();
	    	t8.join();
	    	t9.join();
	    	t10.join();
	    	counter++;
	    }
	    
	    myWriter.write("10 thread:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" + Transactions_729.countNegative + "\n****************************************************************************\n");
	    System.out.println("10 threads:\nNumber of operations:\t" + counter + "\nProducts with stock level less than zero:\t" 
	    	    + Transactions_729.countNegative + "\n****************************************************************************\n");
	    myWriter.close();
	}
}
