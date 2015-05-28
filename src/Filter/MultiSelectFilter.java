package Filter;

import java.util.ArrayList;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

// Container filter that takes a ArrayList<String> and compares them to a given property value
// use it for filtering of containers after multi-selection
public class MultiSelectFilter implements Container.Filter
{

	
	private ArrayList<String> idValues; // List of values to check against
	private String property;			// Name of property to check
	
	public MultiSelectFilter(ArrayList<String> idValues, String property) {
		this.idValues = idValues;
		this.property = property;
	}

	// Check whether an item matches filter criteria
	public boolean passesFilter(Object itemId, Item item)
			throws UnsupportedOperationException {
		// Acquire the relevant property and its value from the item object
        Property p = item.getItemProperty(property);
        String itemValue = (String) p.getValue();
        // Tests for all String in ArrayList
        for (int i = 0; i < idValues.size(); i++)
        {
        	// If match is found, return true 
        	if (itemValue.equals(idValues.get(i))) return true; 
        }
		// If no match was found, return false
        return false;
	}

	
	// Check if comparing against a valid property
	public boolean appliesToProperty(Object propertyId) {
		// TODO Auto-generated method stub
		return propertyId != null &&
	               propertyId.equals(this.property);
	}

}
