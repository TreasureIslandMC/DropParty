package ninja.amp.amplib.messenger;

public interface Message {
	String getMessage();

	void setMessage(String var1);

	String getPath();

	String getDefault();

	String toString();
}
