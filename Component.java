
/**
 * Component.java - an object with a position, rotation, and children, An object which can draw itself.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @author Zezhou Sun <micou@bu.edu>
 * @since Spring 2011
 */

import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class Component implements Rotatable, Nameable, Colorable, UpdatingDisplayable {
	/** The handle to the OpenGL call list to use to draw this component. */
	private int callListHandle;
	/**
	 * The children of this component, which will be drawn respecting the
	 * translation and rotation of this component.
	 */
	private Set<Component> children = new HashSet<Component>();
	/** The color of this component. */
	private FloatColor color = FloatColor.ORANGE;
	/** The displayable object which this component draws. */
	private Displayable displayable;
	/** The position of this component. */
	private Point3D position;
	/** The current angle at which this joint is rotated around the x axis. */
	private double xAngle = 0.0;
	/** The x extent switch **/
	private boolean xExtentEnabled = true;
	/** The minimum angle to which this joint can be rotated around the x axis. */
	private double xNegativeExtent = -360;
	/** The maximum angle to which this joint can be rotated around the x axis. */
	private double xPositiveExtent = 360;
	/** The current angle at which this joint is rotated around the y axis. */
	private double yAngle = 0.0;
	/** The y extent switch **/
	private boolean yExtentEnabled = true;
	/** The minimum angle to which this joint can be rotated around the y axis. */
	private double yNegativeExtent = -360;
	/** The maximum angle to which this joint can be rotated around the y axis. */
	private double yPositiveExtent = 360;
	/** The current angle at which this joint is rotated around the z axis. */
	private double zAngle = 0.0;
	/** The z extent switch **/
	private boolean zExtentEnabled = true;
	/** The minimum angle to which this joint can be rotated around the z axis. */
	private double zNegativeExtent = -360;
	/** The maximum angle to which this joint can be rotated around the z axis. */
	private double zPositiveExtent = 360;
	/** The scaling of current component**/
	private double scale = 1;

	/**
	 * Instantiates this component with the specified position, but with nothing to
	 * display.
	 * 
	 * @param position The position of this component.
	 */
	public Component(final Point3D position, final String name) {
		this(position, null, name);
	}

	public Component(final Point3D position) {
		this(position, null, "default");
	}
	
	public Component(final Point3D position, final double scale) {
		this(position, null, "default");
		this.scale = scale;
	}

	public Component(final Point3D position, final Displayable displayable) {
		this.position = position;
		this.displayable = displayable;
		this.name = "default";
	}

	/**
	 * Instantiates this component with the specified position and the displayable
	 * which this component represents.
	 * 
	 * If the specified displayable object is {@code null}, this component will only
	 * provide a positioning and rotation.
	 * 
	 * @param position    The position of this component.
	 * @param displayable The object which this component represents.
	 */
	public Component(final Point3D position, final Displayable displayable, final String name) {
		this.position = position;
		this.displayable = displayable;
		this.name = name;
	}
	
//	public Component(String name, Coord c, Vivarium v)
//	{
//		
//	}

	public String toString() {
		return "Component[" + ", " + this.xAngle + ", " + this.yAngle + ", " + this.zAngle + ", " + this.position + "]";
	}

	/** The human-readable name of this component. */
	private final String name;

	/**
	 * Adds the specified child to the set of children of this component.
	 * 
	 * @param component The component to add as a child of this component.
	 */
	public void addChild(final Component component) {
		this.children.add(component);
	}

	/**
	 * Convenience method which simply calls the {@link #addChild(Component)} method
	 * for each of the components specified in the parameter list of this method.
	 * 
	 * @param components The components to add as children of this component.
	 */
	public void addChildren(final Component... components) {
		for (final Component component : components) {
			this.addChild(component);
		}
	}

	/**
	 * Calls the OpenGL call list which contains the commands which draw this
	 * component.
	 * 
	 * @param gl The OpenGL object with which to perform the drawing.
	 * 
	 * @see edu.bu.cs.cs480.Displayable#draw(javax.media.opengl.GL)
	 */
	@Override
	public void draw(final GL2 gl) {
		gl.glCallList(this.callListHandle);
	}

	/**
	 * Initializes the call list which this component uses for drawing, then calls
	 * the corresponding method on the children of this component.
	 * 
	 * @param gl The OpenGL object with which to perform the drawing.
	 * 
	 * @see edu.bu.cs.cs480.Displayable#initialize(javax.media.opengl.GL)
	 */
	@Override
	public void initialize(final GL2 gl) {
		// create a new OpenGL call list handle
		this.callListHandle = gl.glGenLists(1);

		// initialize the displayable object which this component represents
		if (this.displayable != null) {
			this.displayable.initialize(gl);
		}

		// initialize each of the children of this component
		for (final Component child : this.children) {
			child.initialize(gl);
		}
	}
	
	/**
	 * Updates the call list used to when this component is drawn.
	 * 
	 * This method first calls the corresponding method on the children of this
	 * component. Then this component is translated, rotated, and colored
	 * appropriately. Next this component is drawn using the {@link Displayable}
	 * specified in the constructor of this class. Finally, the children of this
	 * component are drawn with respect to the rotation and translation done to this
	 * component.
	 * 
	 * @param gl The OpenGL object with which to perform the drawing.
	 * 
	 * @see edu.bu.cs.cs480.UpdatingDisplayable#update(javax.media.opengl.GL)
	 */
	@Override
	public void update(final GL2 gl) {
		// update each of the children of this component
		for (final Component child : this.children) {
			child.update(gl);
		}

		gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
		gl.glPushMatrix();

		// translate this component to where it will be located in the scene
		gl.glTranslated(this.position.x(), this.position.y(), this.position.z());

		// first, rotate this component around each of the three axes
		gl.glRotated(this.xAngle, 1, 0, 0);
		gl.glRotated(this.yAngle, 0, 1, 0);
		gl.glRotated(this.zAngle, 0, 0, 1);
		
		gl.glScaled(this.scale, this.scale, this.scale);

		// draw the displayable which this component represents in its color
		if (this.displayable != null) {
			gl.glPushAttrib(GL2.GL_CURRENT_BIT);
			gl.glColor3f(this.color.red(), this.color.green(), this.color.blue());
			this.displayable.draw(gl);
			gl.glPopAttrib();
		}

		// draw all the children of this component
		for (final Component child : this.children) {
			child.draw(gl);
		}

		gl.glPopMatrix();
		gl.glEndList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param axis        {@inheritDoc}
	 * @param angleDeltan {@inheritDoc}
	 */
	public void rotate(final Axis axis, final double angleDelta) {
		if (axis.equals(Axis.X)) {
			this.xAngle += angleDelta;
		} else if (axis.equals(Axis.Y)) {
			this.yAngle += angleDelta;
		} else if (axis.equals(Axis.Z)) {
			this.zAngle += angleDelta;
		}
		this.limitExtent();
	}
	
	/**
	 * Set displayable object in this class
	 * 
	 * @param axis        {@inheritDoc}
	 * @param angleDeltan {@inheritDoc}
	 */
	public void setDisplayable(Displayable obj) {
		this.displayable = obj;
	}

	/**
	 * Set rotation angle for axis
	 * 
	 * @param axis        {@inheritDoc}
	 * @param angleDeltan {@inheritDoc}
	 */
	public void setAngle(final Axis axis, final double angleDelta) {
		if (axis.equals(Axis.X)) {
			this.xAngle = angleDelta;
		} else if (axis.equals(Axis.Y)) {
			this.yAngle = angleDelta;
		} else if (axis.equals(Axis.Z)) {
			this.zAngle = angleDelta;
		}
		this.limitExtent();
	}

	/**
	 * Set rotation angle for axis
	 * 
	 * @param axis        {@inheritDoc}
	 * @param angleDeltan {@inheritDoc}
	 */
	public void setAngles(final double x, final double y, final double z) {
		this.xAngle = x;
		this.yAngle = y;
		this.zAngle = z;
		this.limitExtent();
	}

	public void setAngles(final Configuration angledObject) {
		this.setAngles(angledObject.xAngle(), angledObject.yAngle(), angledObject.zAngle());
	}
	
	private void limitExtent() {
		if (this.xExtentEnabled) {
			this.xAngle = Math.min(this.xAngle, this.xPositiveExtent);
			this.xAngle = Math.max(this.xAngle, this.xNegativeExtent);
		} 
		if (this.yExtentEnabled) {
			this.yAngle = Math.min(this.yAngle, this.yPositiveExtent);
			this.yAngle = Math.max(this.yAngle, this.yNegativeExtent);
		} 
		if (this.zExtentEnabled) {
			this.zAngle = Math.min(this.zAngle, this.zPositiveExtent);
			this.zAngle = Math.max(this.zAngle, this.zNegativeExtent);
		}
	}
	
	public boolean checkRotationReachedExtent(final Axis axis) {
		if (axis.equals(Axis.X)) {
			return this.xAngle == this.xNegativeExtent || this.xAngle == this.xPositiveExtent;
		} else if (axis.equals(Axis.Y)) {
			return this.yAngle == this.yNegativeExtent || this.yAngle == this.yPositiveExtent;
		} else if (axis.equals(Axis.Z)) {
			return this.zAngle == this.zNegativeExtent || this.zAngle == this.zPositiveExtent;
		}
		return false;
	}
	
	public void setScale(final double scale) {
		this.scale = scale;
	}

	public void setPosition(final Point3D p) {
		this.position = p;
	}

	/** 
	 * Set up component's position and rotations all together
	 * */
	public void setConfiguration(final double x, final double y, final double z, final Point3D p) {
		this.setAngles(x, y, z);
		this.position = p;
	}

	public void setConfiguration(Configuration config) {
		this.setConfiguration(config.xAngle(), config.yAngle(), config.zAngle(), config.position());
	}

	/**
	 * Enable or disable extent checking for current Component
	 */
	public void setExtentSwitch(final boolean b) {
		this.xExtentEnabled = b;
		this.yExtentEnabled = b;
		this.zExtentEnabled = b;
	}

	/**
	 * Enable or disable extent checking for current Component certain Axis
	 */
	public void setExtentSwitch(final Axis axis, final boolean b) {
		if (axis.equals(Axis.X)) {
			this.xExtentEnabled = b;
		} else if (axis.equals(Axis.Y)) {
			this.yExtentEnabled = b;
		} else if (axis.equals(Axis.Z)) {
			this.zExtentEnabled = b;
		}
	}

	/**
	 * Change Configuration on Current configuration
	 * 
	 */
	public void changeConfiguration(Configuration config) {
		this.changeConfiguration(config.xAngle(), config.yAngle(), config.zAngle(), config.position());
	}

	public void changeConfiguration(final double x, final double y, final double z, final Point3D p) {
		this.rotate(Axis.X, x);
		this.rotate(Axis.Y, y);
		this.rotate(Axis.Z, z);
		this.position = new Point3D(this.position.x() + p.x(), this.position.y() + p.y(), this.position.z() + p.z());
	}

	/**
	 * Get current component's Configuration, which includes position and X, Y, Z
	 * rotations
	 * 
	 */
	public Configuration configuration() {
		return new BaseConfiguration(this.xAngle, this.yAngle, this.zAngle, new Point3D(this.position));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param color {@inheritDoc}
	 * 
	 * @see edu.bu.cs.cs480.Colorable#setColor(edu.bu.cs.cs480.FloatColor)
	 */
	@Override
	public void setColor(final FloatColor color) {
		this.color = color;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param newXNegativeExtent {@inheritDoc}
	 */
	public void setXNegativeExtent(final double newXNegativeExtent) {
		this.xNegativeExtent = newXNegativeExtent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param newXPositiveExtent {@inheritDoc}
	 */
	public void setXPositiveExtent(final double newXPositiveExtent) {
		this.xPositiveExtent = newXPositiveExtent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param newYNegativeExtent {@inheritDoc}
	 */
	public void setYNegativeExtent(final double newYNegativeExtent) {
		this.yNegativeExtent = newYNegativeExtent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param newYPositiveExtent {@inheritDoc}
	 */
	public void setYPositiveExtent(final double newYPositiveExtent) {
		this.yPositiveExtent = newYPositiveExtent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param newZNegativeExtent {@inheritDoc}
	 */
	public void setZNegativeExtent(final double newZNegativeExtent) {
		this.zNegativeExtent = newZNegativeExtent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param newZPositiveExtent {@inheritDoc}
	 */
	public void setZPositiveExtent(final double newZPositiveExtent) {
		this.zPositiveExtent = newZPositiveExtent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double xAngle() {
		return this.xAngle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double yAngle() {
		return this.yAngle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double zAngle() {
		return this.zAngle;
	}
	
	public double scale() {
		return this.scale;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public Point3D position() {
		return this.position;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public String name() {
		return this.name;
	}
}
