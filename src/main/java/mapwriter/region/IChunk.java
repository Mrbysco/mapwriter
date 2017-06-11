package mapwriter.region;

import net.minecraft.block.state.IBlockState;

public interface IChunk
{
	public IBlockState getBlockState(int x, int y, int z);

	public int getBiome(int x, int y, int z);

	public int getLightValue(int x, int y, int z);

	public int getMaxY();
}
