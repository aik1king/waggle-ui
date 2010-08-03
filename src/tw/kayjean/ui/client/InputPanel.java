package tw.kayjean.ui.client;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;

import tw.kayjean.ui.client.rpc.LocationsCallback;

public class InputPanel extends Composite {

	private SuggestOracle oracle = new ServerSuggestOracle();
    //private SuggestBox textBox = new SuggestBox(oracle);
	private SuggestBox textBox = new SuggestBox();
    private Collection cLocations;
    
    public InputPanel(final String preset) {
        Panel panel = buildForm();       
        initWidget(panel);
        textBox.setText(preset);
        this.setStyleName("inputPanel");
    }

    /**
     * Prepares the formPanel used in this panel
     * @return new structure of InputPanel
     */
    private Panel buildForm() {
        HorizontalPanel p = new HorizontalPanel();

        AutocompleteHelper acHelp = new AutocompleteHelper();

        // Make an AJAX call to populate the autocompleteS
        //這是最重要的地方,啟動程式運作,將資料送入系統
        Waggle_ui.locsService.getLocations(new LocationsCallback());

        // Add listeners
        textBox.addClickListener(acHelp);
        textBox.addEventHandler(acHelp);
        textBox.addFocusListener(acHelp);
        textBox.addKeyboardListener(acHelp);

        // Add to the panel
        p.add(textBox);
        return p;
    }
    
    /**
     * Populates the autocompletion box given a Collection of
     * Strings (Location entry strings)
     * @param inCollection - A Java Collection of Strings
     */
    public void populateSuggestions(Collection inCollection) {
        ((MultiWordSuggestOracle) textBox.getSuggestOracle()).addAll(inCollection);
        cLocations = inCollection;
    }
    
    public boolean isValidLocation(String loc) {
        return cLocations.contains(loc);
    }
    
    /**
     * Tries to match a given string to a location name
     * @param ls Lower case string
     * @return String if matched, null if not
     */
/*沒有使用到    
    public String lowercaseMatch(String ls) {
        Iterator i = cLocations.iterator();
        while (i.hasNext()) {
            String currLocation = (String) i.next();
            if (currLocation.toLowerCase().equals(ls)) {
                return currLocation;
            }
        }
        return null;
    }
*/
    
    /**
     * A fun method whose sole purpose is to shake the textbox.
     */
//delete    public void shakeIt() {
//delete        Effects.Effect("Shake",textBox);
//delete    }

    /**
     * Tells the LocationPanel to add a new Location
     */
    private void signalToAddLoc() {
        WUF.locPanel.addLocation(textBox.getText());
        textBox.setText("");
        textBox.setFocus(true);
    }

    /** Clicking on the submit button. Is this even really needed anymore?
    private class SubmitListener implements ClickListener {
        public void onClick(Widget sender) {
            signalToAddLoc();
        }
    } **/

    /**
     * Listener for the SuggestBox
     * @author Simon
     */
    private class AutocompleteHelper implements SuggestionHandler, KeyboardListener, ClickListener, FocusListener {
        private boolean beforeFirst = true;
        
        public void onSuggestionSelected(SuggestionEvent event) {
            signalToAddLoc();
        }

        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        //    if (keyCode == KEY_ENTER && !textBox.isCompleting() && !isValidLocation(textBox.getText())) {
        //        CMF.message("Sorry, we don't recognize that location. Please accept one of the suggestions.");
        //        shakeIt();
        //    }
        }
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {}
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {}

        public void onClick(Widget sender) {
            if(beforeFirst) {
                textBox.setText("");
                beforeFirst = false;
            }
        }

        public void onFocus(Widget sender) {
            textBox.addStyleName("textbox_g");
        }

        public void onLostFocus(Widget sender) {
            textBox.removeStyleName("textbox_g");
        }

    }
}