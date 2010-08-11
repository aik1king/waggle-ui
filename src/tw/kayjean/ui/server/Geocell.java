package tw.kayjean.ui.server;

import java.util.HashMap;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Geocell {

	
    private static int MAX_GEOCELL_RESOLUTION = 13; //The maximum *practical* geocell resolution.
    private static int GEOCELL_GRID_SIZE = 4;
    final static char[] GEOCELL_ALPHABET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    final static HashMap<Character, Integer> lookupcell = new HashMap<Character, Integer>();
    static {
        int i = 0;
        for (char c : GEOCELL_ALPHABET)
            lookupcell.put(c, i++);
    }
    
    static private String commonPrefix(String s1, String s2) {
        int i,l1,l2;
        l1 = s1.length();
        l2 = s2.length();
        for(i=0; i<l1 && i<l2; i++) {
                if (s1.charAt(i) != s2.charAt(i)) break;
        }
        return(s1.substring(0, i));
}
    
    
    private double[] compute_box(String cell){
/*    	
    	  """Computes the rectangular boundaries (bounding box) of the given geocell.
    	  
    	  Args:
    	    cell: The geocell string whose boundaries are to be computed.
    	  
    	  Returns:
    	    A geotypes.Box corresponding to the rectangular boundaries of the geocell.
    	  """
*/    	  
    	if( cell == null || cell == "" )
    		return null;

    	  
    	  double bboxnorth = 90;
    	  double bboxeast = 180;
    	  double bboxsouth = -90.0;
    	  double bboxwest = -180;
    	  
    	  while( cell.length() > 0 ){
    	    double subcell_lon_span = (bboxeast - bboxwest) / GEOCELL_GRID_SIZE;
    	    double subcell_lat_span = (bboxnorth - bboxsouth) / GEOCELL_GRID_SIZE;
    	    int[] xy = _subdiv_xy( cell.charAt(0) );
    	    int x = xy[0];
    	    int y = xy[1];
    	    bboxnorth = bboxsouth + subcell_lat_span * (y + 1);
    	    bboxeast = bboxwest + subcell_lon_span * (x + 1);
    	    bboxsouth = bboxsouth + subcell_lat_span * y;
    	    bboxwest = bboxwest + subcell_lon_span * x;
    	    
    	    cell = cell.substring(1);
    	  }
    	  return new double[]{bboxnorth, bboxeast , bboxsouth , bboxwest };
    }

    	  
    private int interpolation_count( String cell_ne, String cell_sw){
/*    	
    	  """Computes the number of cells in the grid created by interpolating from the
    	  given Northeast geocell to the given Southwest geocell.
    	  
    	  Assumes the Northeast geocell is actually Northeast of Southwest geocell.
    	  
    	  Arguments:
    	    cell_ne: The Northeast geocell string.
    	    cell_sw: The Southwest geocell string.
    	  
    	  Returns:
    	    An int, indicating the number of geocells in the interpolation.
    	  """
*/    	  
    	double[] bbox_ne = compute_box(cell_ne);
    	double[] bbox_sw = compute_box(cell_sw);
    	  
    	  return (int)(((bbox_ne[1] - bbox_sw[3]) / (bbox_sw[1] - bbox_sw[3])) * ((bbox_ne[0] - bbox_sw[2]) / (bbox_sw[0] - bbox_sw[2])));
    }
    
    public static void main(String[] args) {
        Geocell e = new Geocell();
        String s = e.compute(25.121829 , 121.744308, MAX_GEOCELL_RESOLUTION);
        String t = s.substring( 0 , 1 );
        List r = e.best_bbox_search_cells(25.121829, 121.744308, 23.121, 120.744308);
    }

    public List best_bbox_search_cells(double north_east_lat ,double north_east_lon ,double south_west_lat ,double south_west_lon ){
/*
    	"""Returns the most efficient set of geocells to search in a bounding box
    	  query, given a cost function.
    	  
    	  This method is guaranteed to return a set of geocells having the same
    	  resolution.
    	  
    	  Args:
    	    bbox: A geotypes.Box indicating the bounding box being searched.
    	    cost_function: A function that accepts two arguments:
    	        * num_cells: the number of cells to search
    	        * resolution: the resolution of each cell to search
    	        and returns the `cost' of querying against this number of cells
    	        at the given resolution.
    	  
    	  Returns:
    	    A list of geocell strings that contain the given box.
    	  """
*/    	  
    	  String cell_ne = compute(north_east_lat,north_east_lon, MAX_GEOCELL_RESOLUTION);
    	  String cell_sw = compute(south_west_lat,south_west_lon, MAX_GEOCELL_RESOLUTION);

    	  //float min_cost = 1e10000;  // # Practical infinity.
    	  float min_cost = Float.MAX_VALUE;  // # Practical infinity.
    	  
    	  List<String> min_cost_cell_set = new ArrayList<String>();
    	  
//    	  # First find the common prefix, if there is one.. this will be the base
//    	  # resolution.. i.e. we don't have to look at any higher resolution cells.
    	  int min_resolution = commonPrefix( cell_sw , cell_ne ).length() + 1;
    	  
//    	  # Iteravely calculate all possible sets of cells that wholely contain
//    	  # the requested bounding box.
    	  
    	  for( int cur_resolution = min_resolution ; cur_resolution < MAX_GEOCELL_RESOLUTION + 1 ; cur_resolution++ ){
    		  String cur_ne = cell_ne.substring(0 , cur_resolution );
    		  String cur_sw = cell_sw.substring(0 , cur_resolution );
    		  
      	    int num_cells = interpolation_count(cur_ne, cur_sw);
    	    if( num_cells > 300)
    	      continue;
    	    
    	    List cell_set = interpolate(cur_ne, cur_sw);
    	    Collections.sort(cell_set);
//    	    simplified_cells = []
  
/*    	                        
    	    '''
    	    # NOTE: this may be moot
    	    # try to simplify the geocells by prefix matching
    	    consecs = {}
    	    for i in range(1, len(cell_set)):
    	      cp = commonprefix([cell_set[i], cell_set[i - 1]])
    	      # increment count for this prefix and all parent prefixes
    	      for s in range(len(cp)):
    	        pfx = cp[:s + 1]
    	        if not pfx in consecs:
    	          consecs[pfx] = 0
    	        elif consecs[pfx] < 0: # this prefix is already marked to be collapsed
    	          continue
    	        else:
    	          consecs[pfx] += 1
    	          if consecs[pfx] >= pow(16, cur_resolution - len(pfx)):
    	            consecs[pfx] = -1 # mark the prefix to be collapsed
    	            simplified_cells.append(pfx)
    	    
    	    print >> sys.stderr, simplified_cells
    	    
    	    for sc in sorted(simplified_cells):
    	      cell_set = filter(lambda c: not c.startswith(sc), cell_set)
    	      simplified_cells = filter(lambda c: c == sc or not c.startswith(sc),
    	                                simplified_cells)
    	    cell_set.extend(simplified_cells)
    	    '''
*/    	    
    	    float cost = 0;
    	    double costfunc = Math.pow(GEOCELL_GRID_SIZE, 2);
    	    if( cell_set.size() > costfunc )
    	    	cost = Float.MAX_VALUE;
    	    else
    	    	cost = 0;
    	    
//    	    # TODO(romannurik): See if this resolution is even possible, as in the 
//    	    # future cells at certain resolutions may not be stored.
    	    if( cost <= min_cost ){
    	      min_cost = cost;
    	      min_cost_cell_set = cell_set;
//    	      #print >> sys.stderr, 'boxes=%d, cost=%f' % (len(cell_set), cost)
    	    }
    	    else
//    	      # Once the cost starts rising, we won't be able to do better, so abort.
    	      break;
    	  }
    	  return min_cost_cell_set;
    }    
    
    
    public String compute( double lat , double lon ){
    	return compute( lat , lon , MAX_GEOCELL_RESOLUTION );
    }
    
    public String compute( double lat , double lon , int resolution ){
    	double north = 90.0;
    	double south = -90.0;
    	double east = 180.0;
    	double west = -180.0;
          StringBuilder cell = new StringBuilder();
    	  while(  cell.length() < resolution ){
    	    double subcell_lon_span = (east - west) / GEOCELL_GRID_SIZE;
    	    double subcell_lat_span = (north - south) / GEOCELL_GRID_SIZE;
    	    
    	    int x = Math.min( (int)(GEOCELL_GRID_SIZE * (lon - west) / (east - west)),
    	            GEOCELL_GRID_SIZE - 1);
    	    int y = Math.min( (int)(GEOCELL_GRID_SIZE * (lat - south) / (north - south)),
    	            GEOCELL_GRID_SIZE - 1);
    	    
    	    cell.append(  _subdiv_char(x,y)  );
    	    
    	    south += subcell_lat_span * y;
    	    north = south + subcell_lat_span;
    	    
    	    west += subcell_lon_span * x;
    	    east = west + subcell_lon_span;
    	  }
    	  return cell.toString();
    }

    private char _subdiv_char(int pos0 , int pos1 ){
    	  return GEOCELL_ALPHABET[(pos1 & 2) << 2 |
    	                	      (pos0 & 2) << 1 |
    	                	      (pos1 & 1) << 1 |
    	                	      (pos0 & 1) << 0];
    }
    
    private Boolean collinear(String cell1, String cell2, int direction ){
    	for( int i = 0 ; i < Math.min(cell1.length() , cell2.length() ) ; i++ ){
    		int[] x1y1 = _subdiv_xy( cell1.charAt(i) );
    		int[] x2y2 = _subdiv_xy( cell2.charAt(i) );
    		
    		if( direction == 0 && x1y1[1] != x2y2[1] )
    			return false;
    		if( direction == 1 && x1y1[0] != x2y2[0] )
    			return false;
    	}
    	return true;
    }
    
    private int[] _subdiv_xy( char c ){
    		int i = lookupcell.get(c);
    	  int x = (i & 4) >> 1 | (i & 1) >> 0;
    	  int y = (i & 8) >> 2 | (i & 2) >> 1;
    	  return new int[]{x, y};
    }
    
    
    private List interpolate(String cell_ne, String cell_sw){
/*    	
    	  """Generates the set of cells in the grid created by interpolating from the
    	  given Northeast geocell to the given Southwest geocell.
    	  
    	  Assumes the Northeast geocell is actually Northeast of Southwest geocell.
    	  
    	  Arguments:
    	    cell_ne: The Northeast geocell string.
    	    cell_sw: The Southwest geocell string.
    	  
    	  Returns:
    	    A list of geocell strings in the interpolation.
    	  """
*/    	  
    	List<String> cell_set = new ArrayList<String>();
    	cell_set.add(cell_sw);
    	
//    	  # 2D array, will later be flattened.
//    	  cell_set = [[cell_sw]]
    	  
//    	  # First get adjacent geocells across until Southeast--collinearity with
//    	  # Northeast in vertical direction (0) means we're at Southeast.
//    	  while not collinear(cell_set[0][-1], cell_ne, 1):
//    	    cell_tmp = adjacent(cell_set[0][-1], (1, 0))
//    	    if cell_tmp is None:
//    	      break
//    	    cell_set[0].append(cell_tmp)
    	    
    	  while( ! collinear( cell_set.get( cell_set.size() - 1 ) , cell_ne , 1 ) ){
    		  String cell_tmp = adjacent(cell_set.get( cell_set.size() -1 ), 1, 0);
    		  if( cell_tmp == null )
    			  break;
    		  cell_set.add(cell_tmp);
    	  }

    	  
//    	  # Then get adjacent geocells upwards.
//    	  while cell_set[-1][-1] != cell_ne:
//    	    cell_tmp_row = [adjacent(g, (0, 1)) for g in cell_set[-1]]
//    	    if cell_tmp_row[0] is None:
//    	      break
//    	    cell_set.append(cell_tmp_row)

    	  int length = cell_set.size();
    	  while( !cell_set.get( cell_set.size() - 1).equals(cell_ne) ){
    		  int size = cell_set.size();
    		  for( int i = size - length ; i < size ; i++ ){
        		  String cell_tmp = adjacent(cell_set.get( i ), 0, 1);
        		  if( cell_tmp == null )
        			  break;
        		  cell_set.add(cell_tmp);
    		  }
    	  }
    	  
//    	  # Flatten cell_set, since it's currently a 2D array.
//    	  cell_set = [g for inner in cell_set for g in inner]
    	              
    	  return cell_set;
    }

    private String adjacent( String cell, int dx , int dy ){
/*    	
    	  """Calculates the geocell adjacent to the given cell in the given direction.
    	  
    	  Args:
    	    cell: The geocell string whose neighbor is being calculated.
    	    dir: A (x, y) tuple indicating direction, where x and y can be -1, 0, or 1.
    	        -1 corresponds to West for x and South for y, and
    	         1 corresponds to East for x and North for y.
    	  
    	  Returns:
    	    The geocell adjacent to the given cell in the given direction, or None if
    	    there is no such cell.
    	  """
*/
    	if( cell == null || cell == "" )
    		return null;

    	char[] cell_adj_arr = cell.toCharArray();
    	int i = cell.length() -1;
    	while( i >= 0 && (dx != 0 || dy != 0 ))
    	{
    		int[] xy = _subdiv_xy( cell_adj_arr[i] );
    		int x = xy[0];
    		int y = xy[1];
    		
    	    //# Horizontal adjacency.
    	    if (dx == -1)  //# Asking for left.
    	    {
    	      if (x == 0)  //# At left of parent cell.
    	        x = GEOCELL_GRID_SIZE - 1;//  # Becomes right edge of adjacent parent.
    	      else{
    	        x -= 1; // # Adjacent, same parent.
    	        dx = 0; // # Done with x.
    	      }
    	    }
    	    else if( dx == 1 ) //  # Asking for right.
    	    {
    	      if (x == GEOCELL_GRID_SIZE - 1) //  # At right of parent cell.
    	        x = 0;  //# Becomes left edge of adjacent parent.
    	      else{
    	        x += 1;  //  # Adjacent, same parent.
    	        dx = 0;  // # Done with x.
    	      }
    	    }
    	        
    	    // # Vertical adjacency.
    	    if (dy == 1) //  # Asking for above.
    	    {
    	      if (y == GEOCELL_GRID_SIZE - 1) // # At top of parent cell.
    	        y = 0; // # Becomes bottom edge of adjacent parent.
    	      else{
    	        y += 1;  //# Adjacent, same parent.
    	        dy = 0;  //# Done with y.
    	      }
    	    }
    	    else if( dy == -1) // # Asking for below.
    	    {
    	      if (y == 0)  //# At bottom of parent cell.
    	        y = GEOCELL_GRID_SIZE - 1; // # Becomes top edge of adjacent parent.
    	      else{
    	        y -= 1;  //# Adjacent, same parent.
    	        dy = 0;  //# Done with y.
    	      }
    	    }
    	    cell_adj_arr[i] = _subdiv_char(x,y);
    	    i -= 1;
    	}

//    	  # If we're not done with y then it's trying to wrap vertically,
//    	  # which is a failure.
    	  if (dy != 0)
    		  return null;
    	  
//    	  # At this point, horizontal wrapping is done inherently.
    	  return String.copyValueOf(cell_adj_arr);
    }    
    
    private List children( String cell){
//    	  """Calculates the immediate children of the given geocell.
//    	  For example, the immediate children of `a' are `a0', `a1', ..., `af'.
//    	  """
    	List<String> cell_set = new ArrayList<String>();
        for (char c : GEOCELL_ALPHABET){
            cell_set.add(cell + c);
        }
        return cell_set;
//    	  return [cell + chr for chr in GEOCELL_ALPHABET]
    }
}
