# MGI-Navigaator

"mvn clean javafx:run" et jooksutada

Viewer is barely functional, will be improved by 19.05


Kasutusjuhend:

Kaardi loomine:
1) Valige "Editor"
2) "File" -> "New", et valida kaardi taustaks pilt
3) "Add vertices", et lisada ristmikuid
3.5) Before adding roads it is crucial to define at least one road preset by first selecting tags in the bottom menu. 
     Tags specify "who" can use the road. For example tags 1 and 2 are selected, tag one has speed=5, tag 2 - speed=50, 
     that means that a traveller defined by tag 1 can traverse this road with speed 5, and a traveller defined by tag 2 - 
     with speed 50, a traveller with tag 3 cannot traverse it at all. 

   Ticking "Congestion?" checkbox specifies that the road can be congested (not yet implemented in viewer), and a congestion 
   function needs to be defined with 3 parameters: peak time - the time in mintues since 00:00 when the congestion reaches 
   its peak(-1 < peak time < 1439); congestion multiplier - the value that the speed is multiplied at the peak 
   (0 <= congestion multiplier < 1); width - an arbitrary value that defines how long congestion lasts (0 < width < infinity),
   50 - 100 is a good value. https://www.desmos.com/calculator/jibvt5akbp (travel time is multiplied by the output of this
   function)
    
   When the preset is complete, "Update preset" button must be pressed.
   
4) "Add edges", et lisada teid, linnuke "Two way?" määrab, kas tegu on kahesuunalise teega.
5) "Delete" abil saab kustutada valesti tehtud ristmikuid ning teid.
6) "File" -> "Save", et salvestada kaart.
7) * "Drag" nupu abil saab kaarti liigutada, paremal olev liugur muudab suurust.

Kaardi vaatamine:
1) Valige "Viewer"
2) "File" -> "Load image", et valida kaardi taustaks pilt
3) "File" -> "Load map", et valida eelnevalt salvestatud kaart
4) "Find" abil saab otsida lühimat teed ühest ristmikust teiseni, tekkinud rippmenüüst saab valida, mis tüüpi teed otsida.
5) * "Drag" nupu abil saab kaarti liigutada, paremal olev liugur muudab suurust.
