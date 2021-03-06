HideShowDragView - Alpha
================

A simple lightweight Android **custom-view** that supports a hide and show **animation** to a user specified position, as well as **dragging**. Alpha version - still under development.

Features:

- Animation between two positions (hide / show)
- Dragging and Dropping
- Fully customizable


Usage
=====

Simply **copy the HideShowDragView.java** file into your project.

Create a <code>CustomView</code> that extends <code>HideShowDragView</code> with the constructor you need
```java
    public class CustomView extends HideShowDragView {
            
        public CustomView(Context context) {
            super(context);
        
            init();
        }
        
        public CustomView(Context context, AttributeSet attrs) {
            super(context, attrs);
        
            init();
        }
        
        private void init() {
            // e.g. inflate a layout
            addView(LayoutInflater.from(context).inflate(R.layout.custom_layout, null));
        }
        
        // ...
    }
```   
    
    
Define your <code>CustomView</code> in <code>.xml</code> (supported container layouts are <code>FrameLayout</code>, <code>RelativeLayout</code> and <code>LinearLayout</code>)

```xml
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent" >

        <your.package.CustomView
            android:id="@+id/customView"
            android:layout_width="90dp"
            android:layout_height="90dp" >
        </your.package.CustomView>
    </FrameLayout>
```    

Initialize it from .xml (when defined in .xml):

```java
    CustomView hideShowView = (CustomView) findViewById(R.id.customView);
```
    
Initialize a new CustomView:

```java
    CustomView hideShowView = new CustomView(Context);
```
    

Inside your <code>Activity</code> or <code>Fragment</code>, use the <code>HideShowDragListener</code> to react to state changes and setup all parameters for your View:

```java
    public class YourActivity extemds Activity implements HideShowDragListener {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.your_layout);
             
            // get the customview
            CustomView hideShowView = (CustomView) findViewById(R.id.customView);
            hideShowView.setHideShowListener(this);
            
            // alternative 1: set the position to which the view should animate from its current position
            hideShowView.setPosition(500, 500);
            // OR:
            // alternative 2: set the positions between the view should animate
            hideShowView.setPositions(0, 0, 500, 500);
            
            // setup other stuff ...
            hideShowView.makeInvisibleOnHide(false);
        
            hideShowView.setDragEnabled(true);
        }
        
        @Override
        public void onHide(HideShowDragView v, float curX, float curY) {

        }
  
        @Override
        public void onShow(HideShowDragView v, float curX, float curY) {

        }
    
        @Override
        public void onDragStart(HideShowDragView v, float startX, float startY) {
 
        }
    
        @Override
        public void onDragFinished(HideShowDragView v, float stopX, float stopY) {
        
        
        }
    }
```
