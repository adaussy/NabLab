/* DO NOT EDIT THIS FILE - it is machine generated */

package explicitheatequation;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import fr.cea.nabla.javalib.mesh.*;

public final class ExplicitHeatEquation
{
	// Mesh and mesh variables
	private final CartesianMesh2D mesh;
	@SuppressWarnings("unused")
	private final int nbNodes, nbCells, nbFaces, maxNodesOfCell, maxNodesOfFace, maxCellsOfFace, maxNeighbourCells;

	// Options and global variables
	private PvdFileWriter2D writer;
	private String outputPath;
	int outputPeriod;
	int lastDump;
	int n;
	double u0;
	static final double[] vectOne = new double[] {1.0, 1.0};
	double stopTime;
	int maxIterations;
	double deltat;
	double t_n;
	double t_nplus1;
	double t_n0;
	double[][] X;
	double[][] Xc;
	double[] u_n;
	double[] u_nplus1;
	double[] V;
	double[] D;
	double[] faceLength;
	double[] faceConductivity;
	double[][] alpha;

	public ExplicitHeatEquation(CartesianMesh2D aMesh)
	{
		// Mesh and mesh variables initialization
		mesh = aMesh;
		nbNodes = mesh.getNbNodes();
		nbCells = mesh.getNbCells();
		nbFaces = mesh.getNbFaces();
		maxNodesOfCell = CartesianMesh2D.MaxNbNodesOfCell;
		maxNodesOfFace = CartesianMesh2D.MaxNbNodesOfFace;
		maxCellsOfFace = CartesianMesh2D.MaxNbCellsOfFace;
		maxNeighbourCells = CartesianMesh2D.MaxNbNeighbourCells;
	}

	public void jsonInit(final String jsonContent)
	{
		final Gson gson = new Gson();
		final JsonObject options = gson.fromJson(jsonContent, JsonObject.class);
		assert(options.has("outputPath"));
		final JsonElement valueof_outputPath = options.get("outputPath");
		outputPath = valueof_outputPath.getAsJsonPrimitive().getAsString();
		writer = new PvdFileWriter2D("ExplicitHeatEquation", outputPath);
		assert(options.has("outputPeriod"));
		final JsonElement valueof_outputPeriod = options.get("outputPeriod");
		assert(valueof_outputPeriod.isJsonPrimitive());
		outputPeriod = valueof_outputPeriod.getAsJsonPrimitive().getAsInt();
		lastDump = Integer.MIN_VALUE;
		if (options.has("u0"))
		{
			final JsonElement valueof_u0 = options.get("u0");
			assert(valueof_u0.isJsonPrimitive());
			u0 = valueof_u0.getAsJsonPrimitive().getAsDouble();
		}
		else
			u0 = 1.0;
		if (options.has("stopTime"))
		{
			final JsonElement valueof_stopTime = options.get("stopTime");
			assert(valueof_stopTime.isJsonPrimitive());
			stopTime = valueof_stopTime.getAsJsonPrimitive().getAsDouble();
		}
		else
			stopTime = 1.0;
		if (options.has("maxIterations"))
		{
			final JsonElement valueof_maxIterations = options.get("maxIterations");
			assert(valueof_maxIterations.isJsonPrimitive());
			maxIterations = valueof_maxIterations.getAsJsonPrimitive().getAsInt();
		}
		else
			maxIterations = 500000000;
		deltat = 0.001;
		X = new double[nbNodes][2];
		Xc = new double[nbCells][2];
		u_n = new double[nbCells];
		u_nplus1 = new double[nbCells];
		V = new double[nbCells];
		D = new double[nbCells];
		faceLength = new double[nbFaces];
		faceConductivity = new double[nbFaces];
		alpha = new double[nbCells][nbCells];

		// Copy node coordinates
		double[][] gNodes = mesh.getGeometry().getNodes();
		IntStream.range(0, nbNodes).parallel().forEach(rNodes ->
		{
			X[rNodes][0] = gNodes[rNodes][0];
			X[rNodes][1] = gNodes[rNodes][1];
		});
	}

	/**
	 * Job computeFaceLength called @1.0 in simulate method.
	 * In variables: X
	 * Out variables: faceLength
	 */
	protected void computeFaceLength()
	{
		IntStream.range(0, nbFaces).parallel().forEach(fFaces -> 
		{
			final int fId = fFaces;
			double reduction0 = 0.0;
			{
				final int[] nodesOfFaceF = mesh.getNodesOfFace(fId);
				final int nbNodesOfFaceF = nodesOfFaceF.length;
				for (int pNodesOfFaceF=0; pNodesOfFaceF<nbNodesOfFaceF; pNodesOfFaceF++)
				{
					final int pId = nodesOfFaceF[pNodesOfFaceF];
					final int pPlus1Id = nodesOfFaceF[(pNodesOfFaceF+1+nbNodesOfFaceF)%nbNodesOfFaceF];
					final int pNodes = pId;
					final int pPlus1Nodes = pPlus1Id;
					reduction0 = sumR0(reduction0, norm(minus(X[pNodes], X[pPlus1Nodes])));
				}
			}
			faceLength[fFaces] = 0.5 * reduction0;
		});
	}

	/**
	 * Job computeTn called @1.0 in executeTimeLoopN method.
	 * In variables: deltat, t_n
	 * Out variables: t_nplus1
	 */
	protected void computeTn()
	{
		t_nplus1 = t_n + deltat;
	}

	/**
	 * Job computeV called @1.0 in simulate method.
	 * In variables: X
	 * Out variables: V
	 */
	protected void computeV()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			final int cId = cCells;
			double reduction0 = 0.0;
			{
				final int[] nodesOfCellC = mesh.getNodesOfCell(cId);
				final int nbNodesOfCellC = nodesOfCellC.length;
				for (int pNodesOfCellC=0; pNodesOfCellC<nbNodesOfCellC; pNodesOfCellC++)
				{
					final int pId = nodesOfCellC[pNodesOfCellC];
					final int pPlus1Id = nodesOfCellC[(pNodesOfCellC+1+nbNodesOfCellC)%nbNodesOfCellC];
					final int pNodes = pId;
					final int pPlus1Nodes = pPlus1Id;
					reduction0 = sumR0(reduction0, det(X[pNodes], X[pPlus1Nodes]));
				}
			}
			V[cCells] = 0.5 * reduction0;
		});
	}

	/**
	 * Job initD called @1.0 in simulate method.
	 * In variables: 
	 * Out variables: D
	 */
	protected void initD()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			D[cCells] = 1.0;
		});
	}

	/**
	 * Job initTime called @1.0 in simulate method.
	 * In variables: 
	 * Out variables: t_n0
	 */
	protected void initTime()
	{
		t_n0 = 0.0;
	}

	/**
	 * Job initXc called @1.0 in simulate method.
	 * In variables: X
	 * Out variables: Xc
	 */
	protected void initXc()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			final int cId = cCells;
			double[] reduction0 = new double[] {0.0, 0.0};
			{
				final int[] nodesOfCellC = mesh.getNodesOfCell(cId);
				final int nbNodesOfCellC = nodesOfCellC.length;
				for (int pNodesOfCellC=0; pNodesOfCellC<nbNodesOfCellC; pNodesOfCellC++)
				{
					final int pId = nodesOfCellC[pNodesOfCellC];
					final int pNodes = pId;
					reduction0 = sumR1(reduction0, X[pNodes]);
				}
			}
			Xc[cCells] = multiply(0.25, reduction0);
		});
	}

	/**
	 * Job updateU called @1.0 in executeTimeLoopN method.
	 * In variables: alpha, u_n
	 * Out variables: u_nplus1
	 */
	protected void updateU()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			final int cId = cCells;
			double reduction0 = 0.0;
			{
				final int[] neighbourCellsC = mesh.getNeighbourCells(cId);
				final int nbNeighbourCellsC = neighbourCellsC.length;
				for (int dNeighbourCellsC=0; dNeighbourCellsC<nbNeighbourCellsC; dNeighbourCellsC++)
				{
					final int dId = neighbourCellsC[dNeighbourCellsC];
					final int dCells = dId;
					reduction0 = sumR0(reduction0, alpha[cCells][dCells] * u_n[dCells]);
				}
			}
			u_nplus1[cCells] = alpha[cCells][cCells] * u_n[cCells] + reduction0;
		});
	}

	/**
	 * Job computeDeltaTn called @2.0 in simulate method.
	 * In variables: D, V
	 * Out variables: deltat
	 */
	protected void computeDeltaTn()
	{
		double reduction0 = Double.MAX_VALUE;
		reduction0 = IntStream.range(0, nbCells).boxed().parallel().reduce
		(
			Double.MAX_VALUE,
			(accu, cCells) ->
			{
				return minR0(accu, V[cCells] / D[cCells]);
			},
			(r1, r2) -> minR0(r1, r2)
		);
		deltat = reduction0 * 0.24;
	}

	/**
	 * Job computeFaceConductivity called @2.0 in simulate method.
	 * In variables: D
	 * Out variables: faceConductivity
	 */
	protected void computeFaceConductivity()
	{
		IntStream.range(0, nbFaces).parallel().forEach(fFaces -> 
		{
			final int fId = fFaces;
			double reduction0 = 1.0;
			{
				final int[] cellsOfFaceF = mesh.getCellsOfFace(fId);
				final int nbCellsOfFaceF = cellsOfFaceF.length;
				for (int c1CellsOfFaceF=0; c1CellsOfFaceF<nbCellsOfFaceF; c1CellsOfFaceF++)
				{
					final int c1Id = cellsOfFaceF[c1CellsOfFaceF];
					final int c1Cells = c1Id;
					reduction0 = prodR0(reduction0, D[c1Cells]);
				}
			}
			double reduction1 = 0.0;
			{
				final int[] cellsOfFaceF = mesh.getCellsOfFace(fId);
				final int nbCellsOfFaceF = cellsOfFaceF.length;
				for (int c2CellsOfFaceF=0; c2CellsOfFaceF<nbCellsOfFaceF; c2CellsOfFaceF++)
				{
					final int c2Id = cellsOfFaceF[c2CellsOfFaceF];
					final int c2Cells = c2Id;
					reduction1 = sumR0(reduction1, D[c2Cells]);
				}
			}
			faceConductivity[fFaces] = 2.0 * reduction0 / reduction1;
		});
	}

	/**
	 * Job initU called @2.0 in simulate method.
	 * In variables: Xc, u0, vectOne
	 * Out variables: u_n
	 */
	protected void initU()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			if (norm(minus(Xc[cCells], vectOne)) < 0.5)
				u_n[cCells] = u0;
			else
				u_n[cCells] = 0.0;
		});
	}

	/**
	 * Job setUpTimeLoopN called @2.0 in simulate method.
	 * In variables: t_n0
	 * Out variables: t_n
	 */
	protected void setUpTimeLoopN()
	{
		t_n = t_n0;
	}

	/**
	 * Job computeAlphaCoeff called @3.0 in simulate method.
	 * In variables: V, Xc, deltat, faceConductivity, faceLength
	 * Out variables: alpha
	 */
	protected void computeAlphaCoeff()
	{
		IntStream.range(0, nbCells).parallel().forEach(cCells -> 
		{
			final int cId = cCells;
			double alphaDiag = 0.0;
			{
				final int[] neighbourCellsC = mesh.getNeighbourCells(cId);
				final int nbNeighbourCellsC = neighbourCellsC.length;
				for (int dNeighbourCellsC=0; dNeighbourCellsC<nbNeighbourCellsC; dNeighbourCellsC++)
				{
					final int dId = neighbourCellsC[dNeighbourCellsC];
					final int dCells = dId;
					final int fId = mesh.getCommonFace(cId, dId);
					final int fFaces = fId;
					final double alphaExtraDiag = deltat / V[cCells] * (faceLength[fFaces] * faceConductivity[fFaces]) / norm(minus(Xc[cCells], Xc[dCells]));
					alpha[cCells][dCells] = alphaExtraDiag;
					alphaDiag = alphaDiag + alphaExtraDiag;
				}
			}
			alpha[cCells][cCells] = 1 - alphaDiag;
		});
	}

	/**
	 * Job executeTimeLoopN called @4.0 in simulate method.
	 * In variables: lastDump, maxIterations, n, outputPeriod, stopTime, t_n, t_nplus1, u_n
	 * Out variables: t_nplus1, u_nplus1
	 */
	protected void executeTimeLoopN()
	{
		n = 0;
		boolean continueLoop = true;
		do
		{
			n++;
			System.out.printf("START ITERATION n: %5d - t: %5.5f - deltat: %5.5f\n", n, t_n, deltat);
			if (n >= lastDump + outputPeriod)
				dumpVariables(n);
		
			computeTn(); // @1.0
			updateU(); // @1.0
		
			// Evaluate loop condition with variables at time n
			continueLoop = (t_nplus1 < stopTime && n + 1 < maxIterations);
		
			t_n = t_nplus1;
			IntStream.range(0, nbCells).parallel().forEach(i1Cells -> 
			{
				u_n[i1Cells] = u_nplus1[i1Cells];
			});
		} while (continueLoop);
		
		System.out.printf("FINAL TIME: %5.5f - deltat: %5.5f\n", t_n, deltat);
		dumpVariables(n+1);
	}

	private static double norm(double[] a)
	{
		return Math.sqrt(dot(a, a));
	}

	private static double dot(double[] a, double[] b)
	{
		double result = 0.0;
		for (int i=0; i<a.length; i++)
		{
			result = result + a[i] * b[i];
		}
		return result;
	}

	private static double det(double[] a, double[] b)
	{
		return (a[0] * b[1] - a[1] * b[0]);
	}

	private static double[] sumR1(double[] a, double[] b)
	{
		return plus(a, b);
	}

	private static double minR0(double a, double b)
	{
		return Math.min(a, b);
	}

	private static double sumR0(double a, double b)
	{
		return a + b;
	}

	private static double prodR0(double a, double b)
	{
		return a * b;
	}

	private static double[] plus(double[] a, double[] b)
	{
		double[] result = new double[a.length];
		for (int ix0=0; ix0<a.length; ix0++)
		{
			result[ix0] = a[ix0] + b[ix0];
		}
		return result;
	}

	private static double[] multiply(double a, double[] b)
	{
		double[] result = new double[b.length];
		for (int ix0=0; ix0<b.length; ix0++)
		{
			result[ix0] = a * b[ix0];
		}
		return result;
	}

	private static double[] minus(double[] a, double[] b)
	{
		double[] result = new double[a.length];
		for (int ix0=0; ix0<a.length; ix0++)
		{
			result[ix0] = a[ix0] - b[ix0];
		}
		return result;
	}

	public void simulate()
	{
		System.out.println("Start execution of explicitHeatEquation");
		computeFaceLength(); // @1.0
		computeV(); // @1.0
		initD(); // @1.0
		initTime(); // @1.0
		initXc(); // @1.0
		computeDeltaTn(); // @2.0
		computeFaceConductivity(); // @2.0
		initU(); // @2.0
		setUpTimeLoopN(); // @2.0
		computeAlphaCoeff(); // @3.0
		executeTimeLoopN(); // @4.0
		System.out.println("End of execution of explicitHeatEquation");
	}

	public static void main(String[] args) throws IOException
	{
		if (args.length == 1)
		{
			final String dataFileName = args[0];
			final Gson gson = new Gson();
			final JsonObject o = gson.fromJson(new FileReader(dataFileName), JsonObject.class);

			// Mesh instanciation
			assert(o.has("mesh"));
			CartesianMesh2D mesh = new CartesianMesh2D();
			mesh.jsonInit(o.get("mesh").toString());

			// Module instanciation(s)
			ExplicitHeatEquation explicitHeatEquation = new ExplicitHeatEquation(mesh);
			if (o.has("explicitHeatEquation")) explicitHeatEquation.jsonInit(o.get("explicitHeatEquation").toString());

			// Start simulation
			explicitHeatEquation.simulate();
		}
		else
		{
			System.err.println("[ERROR] Wrong number of arguments: expected 1, actual " + args.length);
			System.err.println("        Expecting user data file name, for example ExplicitHeatEquation.json");
			System.exit(1);
		}
	}

	private void dumpVariables(int iteration)
	{
		if (!writer.isDisabled())
		{
			try
			{
				Quad[] quads = mesh.getGeometry().getQuads();
				writer.startVtpFile(iteration, t_n, X, quads);
				writer.openNodeData();
				writer.closeNodeData();
				writer.openCellData();
				writer.openCellArray("Temperature", 0);
				for (int i=0 ; i<nbCells ; ++i)
					writer.write(u_n[i]);
				writer.closeCellArray();
				writer.closeCellData();
				writer.closeVtpFile();
				lastDump = n;
			}
			catch (java.io.FileNotFoundException e)
			{
				System.out.println("* WARNING: no dump of variables. FileNotFoundException: " + e.getMessage());
			}
		}
	}
};
