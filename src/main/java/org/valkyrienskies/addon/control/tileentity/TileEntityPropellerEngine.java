package org.valkyrienskies.addon.control.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import org.joml.Vector3dc;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;
import org.valkyrienskies.addon.control.nodenetwork.BasicForceNodeTileEntity;
import org.valkyrienskies.mod.common.ships.ship_world.PhysicsObject;

public class TileEntityPropellerEngine extends BasicForceNodeTileEntity {

    private double propellerAngle;
    private double prevPropellerAngle;
    private boolean isPowered;
    private double propellerAngularVelocity;

    public TileEntityPropellerEngine(Vector3dc normalVeclocityUnoriented,
                                     boolean isForceOutputOriented, double maxThrust) {
        super(normalVeclocityUnoriented, isForceOutputOriented, maxThrust);
        this.isPowered = false;
        this.propellerAngle = Math.random() * 90D;
        this.prevPropellerAngle = propellerAngle;
        this.propellerAngularVelocity = 0;
    }

    public TileEntityPropellerEngine() {
        super();
        this.propellerAngle = Math.random() * 90D;
        this.prevPropellerAngle = propellerAngle;
    }

    public double getPropellerAngle(double partialTicks) {
        double delta = propellerAngle - prevPropellerAngle;
        if (Math.abs(delta) > 180D) {
            delta %= 180D;
            delta += 180D;
        }
        return prevPropellerAngle + delta * partialTicks;
    }

    @Override
    public void update() {
        super.update();
        isPowered = world.isBlockPowered(this.getPos());
        if (isPowered) {
            propellerAngularVelocity++;
        } else {
            propellerAngularVelocity *= Math.max(Math.random(), .9) * 1.05;
            propellerAngularVelocity -= .75 * Math.random() * Math.random();
        }
        propellerAngularVelocity = Math.max(0, Math.min(propellerAngularVelocity, 50));
        prevPropellerAngle = propellerAngle;
        propellerAngle += propellerAngularVelocity;
        propellerAngle %= 360D;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        propellerAngularVelocity = compound.getDouble("propellerAngularVelocity");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setDouble("propellerAngularVelocity", propellerAngularVelocity);
        return super.writeToNBT(compound);
    }

    @Override
    public double getThrustMagnitude(PhysicsObject physicsObject) {
        // region Support for redstone engines
        try {
            // We're reading the world state from the physics thread, so use a try-catch block just to be safe.
            final IBlockState blockState = getWorld().getBlockState(getPos());
            if (blockState.getBlock() == ValkyrienSkiesControl.INSTANCE.vsControlBlocks.redstoneEngine) {
                // Multiply the thrust by the redstone power
                final int redstonePower = getWorld().getRedstonePowerFromNeighbors(getPos());
                return redstonePower * this.getMaxThrust() * this.getThrustMultiplierGoal();
            }
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
        // endregion
        return this.getMaxThrust() * this.getThrustMultiplierGoal();
    }
}
