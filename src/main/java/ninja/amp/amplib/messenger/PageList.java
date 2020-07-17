package ninja.amp.amplib.messenger;

import ninja.amp.amplib.AmpJavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageList {
	private final AmpJavaPlugin plugin;
	private final String name;
	private final int messagesPerPage;
	private List<String> strings;

	public PageList(AmpJavaPlugin plugin, String name, List<String> strings, int messagesPerPage) {
		this.plugin = plugin;
		this.name = name;
		this.messagesPerPage = messagesPerPage;
		this.strings = strings;
	}

	public PageList(AmpJavaPlugin plugin, String name, int messagesPerPage) {
		this.plugin = plugin;
		this.name = name;
		this.messagesPerPage = messagesPerPage;
		this.strings = new ArrayList();
	}

	public int getPageAmount() {
		return (this.strings.size() + this.messagesPerPage - 1) / this.messagesPerPage;
	}

	public void sendPage(int pageNumber, Object recipient) {
		int pageAmount = this.getPageAmount();
		pageNumber = this.clamp(pageNumber, 1, pageAmount);
		Messenger messenger = this.plugin.getMessenger();
		messenger.sendRawMessage(recipient, Messenger.HIGHLIGHT_COLOR + "<-------<| " + Messenger.PRIMARY_COLOR + this.name + ": Page " + pageNumber + "/" + pageAmount + " " + Messenger.HIGHLIGHT_COLOR + "|>------->");
		int startIndex = this.messagesPerPage * (pageNumber - 1);
		Iterator var6 = this.strings.subList(startIndex, Math.min(startIndex + this.messagesPerPage, this.strings.size())).iterator();

		while(var6.hasNext()) {
			String string = (String)var6.next();
			messenger.sendRawMessage(recipient, string);
		}

	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}

	private int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	public static int getPageNumber(String pageNumber) {
		int page;
		try {
			page = Integer.parseInt(pageNumber);
		} catch (NumberFormatException var3) {
			page = 1;
		}

		return page;
	}
}