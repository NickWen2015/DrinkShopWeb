package drinkshop.cp102.server.main;

/**
 * Log（開關設定關閉即可關閉所有Log）
 * ex:Log.showInput("jsonIn");
 **/
public class LogHelper {
	    private static boolean condition = true;  //開關

	    public LogHelper() {super();}

	    public static void showInput(StringBuilder jsonIn) {
	        if (condition)
	        	System.out.println("input: " + String.valueOf(jsonIn));
	    }

	    public static void showOutput(String outText) {
	        if (condition)
	        	System.out.println("output: " + outText);
	}
}
