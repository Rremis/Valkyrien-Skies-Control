package org.valkyrienskies.addon.control.block.custom;

import javax.annotation.Nonnull;

import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.addon.control.fuel.IValkyriumEngine;
import org.valkyrienskies.addon.control.nodenetwork.BasicForceNodeTileEntity;
import org.valkyrienskies.mod.common.ships.ship_world.PhysicsObject;

import valkyrienwarfare.api.TransformType;

public class TileEntityCompressor extends BasicForceNodeTileEntity implements IValkyriumEngine {

	private static final Vector3dc FORCE_NORMAL = new Vector3d(0, 1, 0);
	
	public TileEntityCompressor() {
		super();
	}
	
	public TileEntityCompressor(double maxThrust) {
        this();
        this.setForceOutputNormal(FORCE_NORMAL);
        this.setMaxThrust(maxThrust);
    }
	
	 @Override
	 public Vector3dc getForceOutputNormal(double secondsToApply, PhysicsObject object) {
		 return FORCE_NORMAL;
	 }

    @Override
    public double getThrustMagnitude(PhysicsObject physicsObject) {
    	return this.getMaxThrust() * getThrustMultiplierGoal() * this.getCurrentValkyriumEfficiency(physicsObject);
    }
    
    @Override
    public double getCurrentValkyriumEfficiency(@Nonnull PhysicsObject physicsObject) {
        Vector3d tilePos = new Vector3d(getPos().getX() + .5D, getPos().getY() + .5D,
            getPos().getZ() + .5D);
        physicsObject
            .getShipTransformationManager()
            .getCurrentPhysicsTransform()
            .transformPosition(tilePos, TransformType.SUBSPACE_TO_GLOBAL);
        double yPos = tilePos.y;
        return IValkyriumEngine.getValkyriumEfficiencyFromHeight(yPos);
    }
}
