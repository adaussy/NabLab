/* DO NOT EDIT THIS FILE - it is machine generated */

package hydroremap;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import fr.cea.nabla.javalib.mesh.*;

public final class R1
{
	// Mesh and mesh variables
	private final CartesianMesh2D mesh;
	@SuppressWarnings("unused")
	private final int nbNodes, nbCells;

	// Main module
	private Hydro mainModule;

	// Options and global variables
	double[] rv3;

	public R1(CartesianMesh2D aMesh)
	{
		// Mesh and mesh variables initialization
		mesh = aMesh;
		nbNodes = mesh.getNbNodes();
		nbCells = mesh.getNbCells();
	}

	public void jsonInit(final String jsonContent)
	{
		final Gson gson = new Gson();
		final JsonObject options = gson.fromJson(jsonContent, JsonObject.class);
		rv3 = new double[nbCells];
	}

	/**
	 * Job rj1 called @2.0 in simulate method.
	 * In variables: hv1
	 * Out variables: hv4
	 */
	protected void rj1()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			mainModule.hv4[cCells] = mainModule.hv1[cCells] * 2.0;
		});
	}

	/**
	 * Job rj2 called @3.0 in simulate method.
	 * In variables: hv4
	 * Out variables: rv3
	 */
	protected void rj2()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			rv3[cCells] = mainModule.hv4[cCells] * 3.0;
		});
	}

	public void setMainModule(Hydro aMainModule)
	{
		mainModule = aMainModule;
		mainModule.r1 = this;
	}
};
