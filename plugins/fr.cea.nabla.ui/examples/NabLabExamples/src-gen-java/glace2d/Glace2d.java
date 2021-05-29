/* DO NOT EDIT THIS FILE - it is machine generated */

package glace2d;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import fr.cea.nabla.javalib.*;
import fr.cea.nabla.javalib.mesh.*;

public final class Glace2d
{
	public final static class Options
	{
		public String outputPath;
		public int outputPeriod;
		public double stopTime;
		public int maxIterations;
		public double gamma;
		public double xInterface;
		public double deltatCfl;
		public double rhoIniZg;
		public double rhoIniZd;
		public double pIniZg;
		public double pIniZd;
		public String nonRegression;

		public void jsonInit(final String jsonContent)
		{
			final Gson gson = new Gson();
			final JsonObject o = gson.fromJson(jsonContent, JsonObject.class);
			// outputPath
			assert(o.has("outputPath"));
			final JsonElement valueof_outputPath = o.get("outputPath");
			outputPath = valueof_outputPath.getAsJsonPrimitive().getAsString();
			// outputPeriod
			assert(o.has("outputPeriod"));
			final JsonElement valueof_outputPeriod = o.get("outputPeriod");
			assert(valueof_outputPeriod.isJsonPrimitive());
			outputPeriod = valueof_outputPeriod.getAsJsonPrimitive().getAsInt();
			// stopTime
			if (o.has("stopTime"))
			{
				final JsonElement valueof_stopTime = o.get("stopTime");
				assert(valueof_stopTime.isJsonPrimitive());
				stopTime = valueof_stopTime.getAsJsonPrimitive().getAsDouble();
			}
			else
				stopTime = 0.2;
			// maxIterations
			if (o.has("maxIterations"))
			{
				final JsonElement valueof_maxIterations = o.get("maxIterations");
				assert(valueof_maxIterations.isJsonPrimitive());
				maxIterations = valueof_maxIterations.getAsJsonPrimitive().getAsInt();
			}
			else
				maxIterations = 20000;
			// gamma
			if (o.has("gamma"))
			{
				final JsonElement valueof_gamma = o.get("gamma");
				assert(valueof_gamma.isJsonPrimitive());
				gamma = valueof_gamma.getAsJsonPrimitive().getAsDouble();
			}
			else
				gamma = 1.4;
			// xInterface
			if (o.has("xInterface"))
			{
				final JsonElement valueof_xInterface = o.get("xInterface");
				assert(valueof_xInterface.isJsonPrimitive());
				xInterface = valueof_xInterface.getAsJsonPrimitive().getAsDouble();
			}
			else
				xInterface = 0.5;
			// deltatCfl
			if (o.has("deltatCfl"))
			{
				final JsonElement valueof_deltatCfl = o.get("deltatCfl");
				assert(valueof_deltatCfl.isJsonPrimitive());
				deltatCfl = valueof_deltatCfl.getAsJsonPrimitive().getAsDouble();
			}
			else
				deltatCfl = 0.4;
			// rhoIniZg
			if (o.has("rhoIniZg"))
			{
				final JsonElement valueof_rhoIniZg = o.get("rhoIniZg");
				assert(valueof_rhoIniZg.isJsonPrimitive());
				rhoIniZg = valueof_rhoIniZg.getAsJsonPrimitive().getAsDouble();
			}
			else
				rhoIniZg = 1.0;
			// rhoIniZd
			if (o.has("rhoIniZd"))
			{
				final JsonElement valueof_rhoIniZd = o.get("rhoIniZd");
				assert(valueof_rhoIniZd.isJsonPrimitive());
				rhoIniZd = valueof_rhoIniZd.getAsJsonPrimitive().getAsDouble();
			}
			else
				rhoIniZd = 0.125;
			// pIniZg
			if (o.has("pIniZg"))
			{
				final JsonElement valueof_pIniZg = o.get("pIniZg");
				assert(valueof_pIniZg.isJsonPrimitive());
				pIniZg = valueof_pIniZg.getAsJsonPrimitive().getAsDouble();
			}
			else
				pIniZg = 1.0;
			// pIniZd
			if (o.has("pIniZd"))
			{
				final JsonElement valueof_pIniZd = o.get("pIniZd");
				assert(valueof_pIniZd.isJsonPrimitive());
				pIniZd = valueof_pIniZd.getAsJsonPrimitive().getAsDouble();
			}
			else
				pIniZd = 0.1;
		}
	}

	// Mesh and mesh variables
	private final CartesianMesh2D mesh;
	@SuppressWarnings("unused")
	private final int nbNodes, nbCells, nbInnerNodes, nbTopNodes, nbBottomNodes, nbLeftNodes, nbRightNodes, maxNodesOfCell, maxCellsOfNode;

	// User options
	private final Options options;
	private final PvdFileWriter2D writer;

	// Global variables
	protected int lastDump;
	protected int n;
	protected double t_n;
	protected double t_nplus1;
	protected double t_n0;
	protected double deltat;
	protected double[][] X_n;
	protected double[][] X_nplus1;
	protected double[][] X_n0;
	protected double[][] b;
	protected double[][] bt;
	protected double[][][] Ar;
	protected double[][][] Mt;
	protected double[][] ur;
	protected double[] c;
	protected double[] m;
	protected double[] p;
	protected double[] rho;
	protected double[] e;
	protected double[] E_n;
	protected double[] E_nplus1;
	protected double[] V;
	protected double[] deltatj;
	protected double[][] uj_n;
	protected double[][] uj_nplus1;
	protected double[][] l;
	protected double[][][] Cjr_ic;
	protected double[][][] C;
	protected double[][][] F;
	protected double[][][][] Ajr;

	public Glace2d(CartesianMesh2D aMesh, Options aOptions)
	{
		// Mesh and mesh variables initialization
		mesh = aMesh;
		nbNodes = mesh.getNbNodes();
		nbCells = mesh.getNbCells();
		nbInnerNodes = mesh.getNbInnerNodes();
		nbTopNodes = mesh.getNbTopNodes();
		nbBottomNodes = mesh.getNbBottomNodes();
		nbLeftNodes = mesh.getNbLeftNodes();
		nbRightNodes = mesh.getNbRightNodes();
		maxNodesOfCell = CartesianMesh2D.MaxNbNodesOfCell;
		maxCellsOfNode = CartesianMesh2D.MaxNbCellsOfNode;

		// User options
		options = aOptions;
		writer = new PvdFileWriter2D("Glace2d", options.outputPath);

		// Initialize variables with default values
		lastDump = Integer.MIN_VALUE;

		// Allocate arrays
		X_n = new double[nbNodes][2];
		X_nplus1 = new double[nbNodes][2];
		X_n0 = new double[nbNodes][2];
		b = new double[nbNodes][2];
		bt = new double[nbNodes][2];
		Ar = new double[nbNodes][2][2];
		Mt = new double[nbNodes][2][2];
		ur = new double[nbNodes][2];
		c = new double[nbCells];
		m = new double[nbCells];
		p = new double[nbCells];
		rho = new double[nbCells];
		e = new double[nbCells];
		E_n = new double[nbCells];
		E_nplus1 = new double[nbCells];
		V = new double[nbCells];
		deltatj = new double[nbCells];
		uj_n = new double[nbCells][2];
		uj_nplus1 = new double[nbCells][2];
		l = new double[nbCells][maxNodesOfCell];
		Cjr_ic = new double[nbCells][maxNodesOfCell][2];
		C = new double[nbCells][maxNodesOfCell][2];
		F = new double[nbCells][maxNodesOfCell][2];
		Ajr = new double[nbCells][maxNodesOfCell][2][2];

		// Copy node coordinates
		double[][] gNodes = mesh.getGeometry().getNodes();
		IntStream.range(0, nbNodes).parallel().forEach(rNodes ->
		{
			X_n0[rNodes][0] = gNodes[rNodes][0];
			X_n0[rNodes][1] = gNodes[rNodes][1];
		});
	}

	/**
	 * Job computeCjr called @1.0 in executeTimeLoopN method.
	 * In variables: X_n
	 * Out variables: C
	 */
	protected void computeCjr()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rPlus1Id = nodesOfCellJ[(rNodesOfCellJ+1+nbNodesOfCellJ)%nbNodesOfCellJ];
					final int rMinus1Id = nodesOfCellJ[(rNodesOfCellJ-1+nbNodesOfCellJ)%nbNodesOfCellJ];
					final int rPlus1Nodes = rPlus1Id;
					final int rMinus1Nodes = rMinus1Id;
					C[jCells][rNodesOfCellJ] = ArrayOperations.multiply(0.5, perp(ArrayOperations.minus(X_n[rPlus1Nodes], X_n[rMinus1Nodes])));
				}
			}
		});
	}

	/**
	 * Job computeInternalEnergy called @1.0 in executeTimeLoopN method.
	 * In variables: E_n, uj_n
	 * Out variables: e
	 */
	protected void computeInternalEnergy()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			e[jCells] = E_n[jCells] - 0.5 * dot(uj_n[jCells], uj_n[jCells]);
		});
	}

	/**
	 * Job iniCjrIc called @1.0 in simulate method.
	 * In variables: X_n0
	 * Out variables: Cjr_ic
	 */
	protected void iniCjrIc()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rPlus1Id = nodesOfCellJ[(rNodesOfCellJ+1+nbNodesOfCellJ)%nbNodesOfCellJ];
					final int rMinus1Id = nodesOfCellJ[(rNodesOfCellJ-1+nbNodesOfCellJ)%nbNodesOfCellJ];
					final int rPlus1Nodes = rPlus1Id;
					final int rMinus1Nodes = rMinus1Id;
					Cjr_ic[jCells][rNodesOfCellJ] = ArrayOperations.multiply(0.5, perp(ArrayOperations.minus(X_n0[rPlus1Nodes], X_n0[rMinus1Nodes])));
				}
			}
		});
	}

	/**
	 * Job iniTime called @1.0 in simulate method.
	 * In variables: 
	 * Out variables: t_n0
	 */
	protected void iniTime()
	{
		t_n0 = 0.0;
	}

	/**
	 * Job computeLjr called @2.0 in executeTimeLoopN method.
	 * In variables: C
	 * Out variables: l
	 */
	protected void computeLjr()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					l[jCells][rNodesOfCellJ] = norm(C[jCells][rNodesOfCellJ]);
				}
			}
		});
	}

	/**
	 * Job computeV called @2.0 in executeTimeLoopN method.
	 * In variables: C, X_n
	 * Out variables: V
	 */
	protected void computeV()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			double reduction0 = 0.0;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rId = nodesOfCellJ[rNodesOfCellJ];
					final int rNodes = rId;
					reduction0 = sumR0(reduction0, dot(C[jCells][rNodesOfCellJ], X_n[rNodes]));
				}
			}
			V[jCells] = 0.5 * reduction0;
		});
	}

	/**
	 * Job initialize called @2.0 in simulate method.
	 * In variables: Cjr_ic, X_n0, gamma, pIniZd, pIniZg, rhoIniZd, rhoIniZg, xInterface
	 * Out variables: E_n, m, p, rho, uj_n
	 */
	protected void initialize()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			double rho_ic;
			double p_ic;
			double[] reduction0 = new double[] {0.0, 0.0};
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rId = nodesOfCellJ[rNodesOfCellJ];
					final int rNodes = rId;
					reduction0 = sumR1(reduction0, X_n0[rNodes]);
				}
			}
			final double[] center = ArrayOperations.multiply(0.25, reduction0);
			if (center[0] < options.xInterface)
			{
				rho_ic = options.rhoIniZg;
				p_ic = options.pIniZg;
			}
			else
			{
				rho_ic = options.rhoIniZd;
				p_ic = options.pIniZd;
			}
			double reduction1 = 0.0;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rId = nodesOfCellJ[rNodesOfCellJ];
					final int rNodes = rId;
					reduction1 = sumR0(reduction1, dot(Cjr_ic[jCells][rNodesOfCellJ], X_n0[rNodes]));
				}
			}
			final double V_ic = 0.5 * reduction1;
			m[jCells] = rho_ic * V_ic;
			p[jCells] = p_ic;
			rho[jCells] = rho_ic;
			E_n[jCells] = p_ic / ((options.gamma - 1.0) * rho_ic);
			uj_n[jCells] = new double[] {0.0, 0.0};
		});
	}

	/**
	 * Job setUpTimeLoopN called @2.0 in simulate method.
	 * In variables: X_n0, t_n0
	 * Out variables: X_n, t_n
	 */
	protected void setUpTimeLoopN()
	{
		t_n = t_n0;
		IntStream.range(0, nbNodes).parallel().forEach(i1Nodes -> 
		{
			for (int i1=0; i1<2; i1++)
			{
				X_n[i1Nodes][i1] = X_n0[i1Nodes][i1];
			}
		});
	}

	/**
	 * Job computeDensity called @3.0 in executeTimeLoopN method.
	 * In variables: V, m
	 * Out variables: rho
	 */
	protected void computeDensity()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			rho[jCells] = m[jCells] / V[jCells];
		});
	}

	/**
	 * Job executeTimeLoopN called @3.0 in simulate method.
	 * In variables: E_n, X_n, t_n, uj_n
	 * Out variables: E_nplus1, X_nplus1, t_nplus1, uj_nplus1
	 */
	protected void executeTimeLoopN()
	{
		n = 0;
		boolean continueLoop = true;
		do
		{
			n++;
			System.out.printf("START ITERATION n: %5d - t: %5.5f - deltat: %5.5f\n", n, t_n, deltat);
			if (n >= lastDump + options.outputPeriod)
				dumpVariables(n);
		
			computeCjr(); // @1.0
			computeInternalEnergy(); // @1.0
			computeLjr(); // @2.0
			computeV(); // @2.0
			computeDensity(); // @3.0
			computeEOSp(); // @4.0
			computeEOSc(); // @5.0
			computeAjr(); // @6.0
			computedeltatj(); // @6.0
			computeAr(); // @7.0
			computeBr(); // @7.0
			computeDt(); // @7.0
			computeBoundaryConditions(); // @8.0
			computeBt(); // @8.0
			computeMt(); // @8.0
			computeTn(); // @8.0
			computeU(); // @9.0
			computeFjr(); // @10.0
			computeXn(); // @10.0
			computeEn(); // @11.0
			computeUn(); // @11.0
		
			// Evaluate loop condition with variables at time n
			continueLoop = (t_nplus1 < options.stopTime && n + 1 < options.maxIterations);
		
			t_n = t_nplus1;
			IntStream.range(0, nbNodes).parallel().forEach(i1Nodes -> 
			{
				for (int i1=0; i1<2; i1++)
				{
					X_n[i1Nodes][i1] = X_nplus1[i1Nodes][i1];
				}
			});
			IntStream.range(0, nbCells).parallel().forEach(i1Cells -> 
			{
				E_n[i1Cells] = E_nplus1[i1Cells];
			});
			IntStream.range(0, nbCells).parallel().forEach(i1Cells -> 
			{
				for (int i1=0; i1<2; i1++)
				{
					uj_n[i1Cells][i1] = uj_nplus1[i1Cells][i1];
				}
			});
		} while (continueLoop);
		
		System.out.printf("FINAL TIME: %5.5f - deltat: %5.5f\n", t_n, deltat);
		dumpVariables(n+1);
	}

	/**
	 * Job computeEOSp called @4.0 in executeTimeLoopN method.
	 * In variables: e, gamma, rho
	 * Out variables: p
	 */
	protected void computeEOSp()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			p[jCells] = (options.gamma - 1.0) * rho[jCells] * e[jCells];
		});
	}

	/**
	 * Job computeEOSc called @5.0 in executeTimeLoopN method.
	 * In variables: gamma, p, rho
	 * Out variables: c
	 */
	protected void computeEOSc()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			c[jCells] = Math.sqrt(options.gamma * p[jCells] / rho[jCells]);
		});
	}

	/**
	 * Job computeAjr called @6.0 in executeTimeLoopN method.
	 * In variables: C, c, l, rho
	 * Out variables: Ajr
	 */
	protected void computeAjr()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					Ajr[jCells][rNodesOfCellJ] = ArrayOperations.multiply(((rho[jCells] * c[jCells]) / l[jCells][rNodesOfCellJ]), tensProduct(C[jCells][rNodesOfCellJ], C[jCells][rNodesOfCellJ]));
				}
			}
		});
	}

	/**
	 * Job computedeltatj called @6.0 in executeTimeLoopN method.
	 * In variables: V, c, l
	 * Out variables: deltatj
	 */
	protected void computedeltatj()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			double reduction0 = 0.0;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					reduction0 = sumR0(reduction0, l[jCells][rNodesOfCellJ]);
				}
			}
			deltatj[jCells] = 2.0 * V[jCells] / (c[jCells] * reduction0);
		});
	}

	/**
	 * Job computeAr called @7.0 in executeTimeLoopN method.
	 * In variables: Ajr
	 * Out variables: Ar
	 */
	protected void computeAr()
	{
		IntStream.range(0, nbNodes).parallel().forEach(rNodes -> 
		{
			final int rId = rNodes;
			double[][] reduction0 = new double[][] {{0.0, 0.0}, {0.0, 0.0}};
			{
				final int[] cellsOfNodeR = mesh.getCellsOfNode(rId);
				final int nbCellsOfNodeR = cellsOfNodeR.length;
				for (int jCellsOfNodeR=0; jCellsOfNodeR<nbCellsOfNodeR; jCellsOfNodeR++)
				{
					final int jId = cellsOfNodeR[jCellsOfNodeR];
					final int jCells = jId;
					final int rNodesOfCellJ = Utils.indexOf(mesh.getNodesOfCell(jId), rId);
					reduction0 = sumR2(reduction0, Ajr[jCells][rNodesOfCellJ]);
				}
			}
			for (int i1=0; i1<2; i1++)
			{
				for (int i2=0; i2<2; i2++)
				{
					Ar[rNodes][i1][i2] = reduction0[i1][i2];
				}
			}
		});
	}

	/**
	 * Job computeBr called @7.0 in executeTimeLoopN method.
	 * In variables: Ajr, C, p, uj_n
	 * Out variables: b
	 */
	protected void computeBr()
	{
		IntStream.range(0, nbNodes).parallel().forEach(rNodes -> 
		{
			final int rId = rNodes;
			double[] reduction0 = new double[] {0.0, 0.0};
			{
				final int[] cellsOfNodeR = mesh.getCellsOfNode(rId);
				final int nbCellsOfNodeR = cellsOfNodeR.length;
				for (int jCellsOfNodeR=0; jCellsOfNodeR<nbCellsOfNodeR; jCellsOfNodeR++)
				{
					final int jId = cellsOfNodeR[jCellsOfNodeR];
					final int jCells = jId;
					final int rNodesOfCellJ = Utils.indexOf(mesh.getNodesOfCell(jId), rId);
					reduction0 = sumR1(reduction0, ArrayOperations.plus(ArrayOperations.multiply(p[jCells], C[jCells][rNodesOfCellJ]), matVectProduct(Ajr[jCells][rNodesOfCellJ], uj_n[jCells])));
				}
			}
			for (int i1=0; i1<2; i1++)
			{
				b[rNodes][i1] = reduction0[i1];
			}
		});
	}

	/**
	 * Job computeDt called @7.0 in executeTimeLoopN method.
	 * In variables: deltatCfl, deltatj, stopTime, t_n
	 * Out variables: deltat
	 */
	protected void computeDt()
	{
		double reduction0 = Double.MAX_VALUE;
		reduction0 = IntStream.range(0, nbCells).boxed().parallel().reduce
		(
			Double.MAX_VALUE,
			(accu, jCells) ->
			{
				return minR0(accu, deltatj[jCells]);
			},
			(r1, r2) -> minR0(r1, r2)
		);
		deltat = Math.min((options.deltatCfl * reduction0), (options.stopTime - t_n));
	}

	/**
	 * Job computeBoundaryConditions called @8.0 in executeTimeLoopN method.
	 * In variables: Ar, b
	 * Out variables: Mt, bt
	 */
	protected void computeBoundaryConditions()
	{
		final double[][] I = new double[][] {new double[] {1.0, 0.0}, new double[] {0.0, 1.0}};
		{
			final int[] topNodes = mesh.getTopNodes();
			final int nbTopNodes = topNodes.length;
			IntStream.range(0, nbTopNodes).parallel().forEach(rTopNodes -> 
			{
				final int rId = topNodes[rTopNodes];
				final int rNodes = rId;
				final double[] N = new double[] {0.0, 1.0};
				final double[][] NxN = tensProduct(N, N);
				final double[][] IcP = ArrayOperations.minus(I, NxN);
				bt[rNodes] = matVectProduct(IcP, b[rNodes]);
				Mt[rNodes] = ArrayOperations.plus(ArrayOperations.multiply(IcP, (ArrayOperations.multiply(Ar[rNodes], IcP))), ArrayOperations.multiply(NxN, trace(Ar[rNodes])));
			});
		}
		{
			final int[] bottomNodes = mesh.getBottomNodes();
			final int nbBottomNodes = bottomNodes.length;
			IntStream.range(0, nbBottomNodes).parallel().forEach(rBottomNodes -> 
			{
				final int rId = bottomNodes[rBottomNodes];
				final int rNodes = rId;
				final double[] N = new double[] {0.0, -1.0};
				final double[][] NxN = tensProduct(N, N);
				final double[][] IcP = ArrayOperations.minus(I, NxN);
				bt[rNodes] = matVectProduct(IcP, b[rNodes]);
				Mt[rNodes] = ArrayOperations.plus(ArrayOperations.multiply(IcP, (ArrayOperations.multiply(Ar[rNodes], IcP))), ArrayOperations.multiply(NxN, trace(Ar[rNodes])));
			});
		}
		{
			final int[] leftNodes = mesh.getLeftNodes();
			final int nbLeftNodes = leftNodes.length;
			IntStream.range(0, nbLeftNodes).parallel().forEach(rLeftNodes -> 
			{
				final int rId = leftNodes[rLeftNodes];
				final int rNodes = rId;
				for (int i1=0; i1<2; i1++)
				{
					for (int i2=0; i2<2; i2++)
					{
						Mt[rNodes][i1][i2] = I[i1][i2];
					}
				}
				bt[rNodes] = new double[] {0.0, 0.0};
			});
		}
		{
			final int[] rightNodes = mesh.getRightNodes();
			final int nbRightNodes = rightNodes.length;
			IntStream.range(0, nbRightNodes).parallel().forEach(rRightNodes -> 
			{
				final int rId = rightNodes[rRightNodes];
				final int rNodes = rId;
				for (int i1=0; i1<2; i1++)
				{
					for (int i2=0; i2<2; i2++)
					{
						Mt[rNodes][i1][i2] = I[i1][i2];
					}
				}
				bt[rNodes] = new double[] {0.0, 0.0};
			});
		}
	}

	/**
	 * Job computeBt called @8.0 in executeTimeLoopN method.
	 * In variables: b
	 * Out variables: bt
	 */
	protected void computeBt()
	{
		{
			final int[] innerNodes = mesh.getInnerNodes();
			final int nbInnerNodes = innerNodes.length;
			IntStream.range(0, nbInnerNodes).parallel().forEach(rInnerNodes -> 
			{
				final int rId = innerNodes[rInnerNodes];
				final int rNodes = rId;
				for (int i1=0; i1<2; i1++)
				{
					bt[rNodes][i1] = b[rNodes][i1];
				}
			});
		}
	}

	/**
	 * Job computeMt called @8.0 in executeTimeLoopN method.
	 * In variables: Ar
	 * Out variables: Mt
	 */
	protected void computeMt()
	{
		{
			final int[] innerNodes = mesh.getInnerNodes();
			final int nbInnerNodes = innerNodes.length;
			IntStream.range(0, nbInnerNodes).parallel().forEach(rInnerNodes -> 
			{
				final int rId = innerNodes[rInnerNodes];
				final int rNodes = rId;
				for (int i1=0; i1<2; i1++)
				{
					for (int i2=0; i2<2; i2++)
					{
						Mt[rNodes][i1][i2] = Ar[rNodes][i1][i2];
					}
				}
			});
		}
	}

	/**
	 * Job computeTn called @8.0 in executeTimeLoopN method.
	 * In variables: deltat, t_n
	 * Out variables: t_nplus1
	 */
	protected void computeTn()
	{
		t_nplus1 = t_n + deltat;
	}

	/**
	 * Job computeU called @9.0 in executeTimeLoopN method.
	 * In variables: Mt, bt
	 * Out variables: ur
	 */
	protected void computeU()
	{
		IntStream.range(0, nbNodes).parallel().forEach(rNodes -> 
		{
			ur[rNodes] = matVectProduct(inverse(Mt[rNodes]), bt[rNodes]);
		});
	}

	/**
	 * Job computeFjr called @10.0 in executeTimeLoopN method.
	 * In variables: Ajr, C, p, uj_n, ur
	 * Out variables: F
	 */
	protected void computeFjr()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rId = nodesOfCellJ[rNodesOfCellJ];
					final int rNodes = rId;
					F[jCells][rNodesOfCellJ] = ArrayOperations.plus(ArrayOperations.multiply(p[jCells], C[jCells][rNodesOfCellJ]), matVectProduct(Ajr[jCells][rNodesOfCellJ], (ArrayOperations.minus(uj_n[jCells], ur[rNodes]))));
				}
			}
		});
	}

	/**
	 * Job computeXn called @10.0 in executeTimeLoopN method.
	 * In variables: X_n, deltat, ur
	 * Out variables: X_nplus1
	 */
	protected void computeXn()
	{
		IntStream.range(0, nbNodes).parallel().forEach(rNodes -> 
		{
			X_nplus1[rNodes] = ArrayOperations.plus(X_n[rNodes], ArrayOperations.multiply(deltat, ur[rNodes]));
		});
	}

	/**
	 * Job computeEn called @11.0 in executeTimeLoopN method.
	 * In variables: E_n, F, deltat, m, ur
	 * Out variables: E_nplus1
	 */
	protected void computeEn()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			double reduction0 = 0.0;
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					final int rId = nodesOfCellJ[rNodesOfCellJ];
					final int rNodes = rId;
					reduction0 = sumR0(reduction0, dot(F[jCells][rNodesOfCellJ], ur[rNodes]));
				}
			}
			E_nplus1[jCells] = E_n[jCells] - (deltat / m[jCells]) * reduction0;
		});
	}

	/**
	 * Job computeUn called @11.0 in executeTimeLoopN method.
	 * In variables: F, deltat, m, uj_n
	 * Out variables: uj_nplus1
	 */
	protected void computeUn()
	{
		IntStream.range(0, nbCells).parallel().forEach(jCells -> 
		{
			final int jId = jCells;
			double[] reduction0 = new double[] {0.0, 0.0};
			{
				final int[] nodesOfCellJ = mesh.getNodesOfCell(jId);
				final int nbNodesOfCellJ = nodesOfCellJ.length;
				for (int rNodesOfCellJ=0; rNodesOfCellJ<nbNodesOfCellJ; rNodesOfCellJ++)
				{
					reduction0 = sumR1(reduction0, F[jCells][rNodesOfCellJ]);
				}
			}
			uj_nplus1[jCells] = ArrayOperations.minus(uj_n[jCells], ArrayOperations.multiply((deltat / m[jCells]), reduction0));
		});
	}

	private static double det(double[][] a)
	{
		return a[0][0] * a[1][1] - a[0][1] * a[1][0];
	}

	private static double[] perp(double[] a)
	{
		return new double[] {a[1], -a[0]};
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

	private static double norm(double[] a)
	{
		return Math.sqrt(dot(a, a));
	}

	private static double[][] tensProduct(double[] a, double[] b)
	{
		double[][] result = new double[a.length][a.length];
		for (int ia=0; ia<a.length; ia++)
		{
			for (int ib=0; ib<a.length; ib++)
			{
				result[ia][ib] = a[ia] * b[ib];
			}
		}
		return result;
	}

	private static double[] matVectProduct(double[][] a, double[] b)
	{
		double[] result = new double[a.length];
		for (int ix=0; ix<a.length; ix++)
		{
			double[] tmp = new double[a[0].length];
			for (int iy=0; iy<a[0].length; iy++)
			{
				tmp[iy] = a[ix][iy];
			}
			result[ix] = dot(tmp, b);
		}
		return result;
	}

	private static double trace(double[][] a)
	{
		double result = 0.0;
		for (int ia=0; ia<a.length; ia++)
		{
			result = result + a[ia][ia];
		}
		return result;
	}

	private static double[][] inverse(double[][] a)
	{
		final double alpha = 1.0 / det(a);
		return new double[][] {new double[] {a[1][1] * alpha, -a[0][1] * alpha}, new double[] {-a[1][0] * alpha, a[0][0] * alpha}};
	}

	private static double[] sumR1(double[] a, double[] b)
	{
		return ArrayOperations.plus(a, b);
	}

	private static double sumR0(double a, double b)
	{
		return a + b;
	}

	private static double[][] sumR2(double[][] a, double[][] b)
	{
		return ArrayOperations.plus(a, b);
	}

	private static double minR0(double a, double b)
	{
		return Math.min(a, b);
	}

	public void simulate()
	{
		System.out.println("Start execution of glace2d");
		iniCjrIc(); // @1.0
		iniTime(); // @1.0
		initialize(); // @2.0
		setUpTimeLoopN(); // @2.0
		executeTimeLoopN(); // @3.0
		System.out.println("End of execution of glace2d");
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
			CartesianMesh2DFactory meshFactory = new CartesianMesh2DFactory();
			meshFactory.jsonInit(o.get("mesh").toString());
			CartesianMesh2D mesh = meshFactory.create();

			// Module instanciation(s)
			Glace2d.Options glace2dOptions = new Glace2d.Options();
			if (o.has("glace2d")) glace2dOptions.jsonInit(o.get("glace2d").toString());
			Glace2d glace2d = new Glace2d(mesh, glace2dOptions);

			// Start simulation
			glace2d.simulate();
		}
		else
		{
			System.err.println("[ERROR] Wrong number of arguments: expected 1, actual " + args.length);
			System.err.println("        Expecting user data file name, for example Glace2d.json");
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
				writer.startVtpFile(iteration, t_n, X_n, quads);
				writer.openNodeData();
				writer.closeNodeData();
				writer.openCellData();
				writer.openCellArray("Density", 0);
				for (int i=0 ; i<nbCells ; ++i)
					writer.write(rho[i]);
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
