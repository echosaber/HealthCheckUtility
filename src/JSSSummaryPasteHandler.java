import javax.swing.*;


public class JSSSummaryPasteHandler extends JFrame{

    public JSSSummaryPasteHandler(){
        JOptionPane.showMessageDialog(null, "Unable to get the JSS Summary with the supplied account. \nYou have encountered a JSS oddity that causes some accounts to not be able to access the summary.\nPlease create a new account with at least read privileges, and try again.", "JSS Summary Error", JOptionPane.ERROR_MESSAGE);
    }

}
