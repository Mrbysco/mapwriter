package mapwriter.overlay;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import mapwriter.api.ILabelInfo;
import mapwriter.api.IMapMode;
import mapwriter.api.IMapView;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;

public class OverlaySlime implements IMwDataProvider
{
	public static boolean seedFound = false;
	public static boolean seedAsked = false;
	private static long seed = -1;

	public static void setSeed(long seed)
	{
		OverlaySlime.seed = seed;
		OverlaySlime.seedFound = true;
	}

	public static void askSeed()
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player == null)
		{
			return;
		}
		player.sendChatMessage("/seed"); // Send the /seed command to the server
		seedAsked = true;
	}

	public static void reset()
	{
		seedFound = false;
		seedAsked = false;
		seed = -1;
	}

	public class ChunkOverlay implements IMwChunkOverlay
	{
		Point coord;

		public ChunkOverlay(int x, int z)
		{
			this.coord = new Point(x, z);
		}

		@Override
		public Point getCoordinates()
		{
			return this.coord;
		}

		@Override
		public int getColor()
		{
			return 0x5000ff00;
		}

		@Override
		public float getFilling()
		{
			return 1.0f;
		}

		@Override
		public boolean hasBorder()
		{
			return true;
		}

		@Override
		public float getBorderWidth()
		{
			return 0.5f;
		}

		@Override
		public int getBorderColor()
		{
			return 0xff000000;
		}

	}

	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX,
			double minZ, double maxX, double maxZ)
	{
		// We should pass the center of the map too to reduce the display like
		// in this case
		// and the zoom lvl, to provide higher level informations

		if (Minecraft.getMinecraft().player.getEntityWorld().provider.getDimensionType().getId() != dim)
		{
			return new ArrayList<IMwChunkOverlay>();
		}

		int minChunkX = (MathHelper.ceil(minX) >> 4) - 1;
		int minChunkZ = (MathHelper.ceil(minZ) >> 4) - 1;
		int maxChunkX = (MathHelper.ceil(maxX) >> 4) + 1;
		int maxChunkZ = (MathHelper.ceil(maxZ) >> 4) + 1;
		int cX = (MathHelper.ceil(centerX) >> 4) + 1;
		int cZ = (MathHelper.ceil(centerZ) >> 4) + 1;

		int limitMinX = Math.max(minChunkX, cX - 100);
		int limitMaxX = Math.min(maxChunkX, cX + 100);
		int limitMinZ = Math.max(minChunkZ, cZ - 100);
		int limitMaxZ = Math.min(maxChunkZ, cZ + 100);

		if (!seedFound && !seedAsked)
		{
			// We don't have the seed and we didn't ask for it yet. Let's go!
			askSeed();
		}

		ArrayList<IMwChunkOverlay> chunks = new ArrayList<IMwChunkOverlay>();
		if (seedFound)
		{ // If we know the seed, then add the overlay
			for (int x = limitMinX; x <= limitMaxX; x++)
			{
				for (int z = limitMinZ; z <= limitMaxZ; z++)
				{

					Random rnd =
							new Random(
									(seed + (x * x * 0x4c1906) + (x * 0x5ac0db) + (z * z * 0x4307a7L) + (z * 0x5f24f)) ^
											0x3ad8025f);
					if (rnd.nextInt(10) == 0)
					{
						chunks.add(new ChunkOverlay(x, z));
					}
				}
			}
		}

		return chunks;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ)
	{
		return "";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, IMapView mapview)
	{
	}

	@Override
	public void onDimensionChanged(int dimension, IMapView mapview)
	{
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, IMapView mapview)
	{

	}

	@Override
	public void onZoomChanged(int level, IMapView mapview)
	{

	}

	@Override
	public void onOverlayActivated(IMapView mapview)
	{

	}

	@Override
	public void onOverlayDeactivated(IMapView mapview)
	{

	}

	@Override
	public void onDraw(IMapView mapview, IMapMode mapmode)
	{

	}

	@Override
	public boolean onMouseInput(IMapView mapview, IMapMode mapmode)
	{
		return false;
	}

	@Override
	public ILabelInfo getLabelInfo(int mouseX, int mouseY)
	{
		return null;
	}

}
