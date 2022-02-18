"""
DO NOT EDIT THIS FILE - it is machine generated
"""
import sys
import json
import numpy as np
import math
from cartesianmesh2d import CartesianMesh2D
from pvdfilewriter2d import PvdFileWriter2D

class HeatEquation:
	PI = 3.1415926
	alpha = 1.0
	deltat = 0.001

	def __init__(self, mesh):
		self.__mesh = mesh
		self.__nbNodes = mesh.nbNodes
		self.__nbCells = mesh.nbCells
		self.__nbFaces = mesh.nbFaces

	def jsonInit(self, jsonContent):
		self.__outputPath = jsonContent["outputPath"]
		self.__writer = PvdFileWriter2D("HeatEquation", self.__outputPath)
		self.outputPeriod = jsonContent["outputPeriod"]
		self.lastDump = -sys.maxsize - 1
		self.n = 0
		self.stopTime = jsonContent["stopTime"]
		self.maxIterations = jsonContent["maxIterations"]
		self.X = np.empty((self.__nbNodes, 2), dtype=np.double)
		self.center = np.empty((self.__nbCells, 2), dtype=np.double)
		self.u_n = np.empty((self.__nbCells), dtype=np.double)
		self.u_nplus1 = np.empty((self.__nbCells), dtype=np.double)
		self.V = np.empty((self.__nbCells), dtype=np.double)
		self.f = np.empty((self.__nbCells), dtype=np.double)
		self.outgoingFlux = np.empty((self.__nbCells), dtype=np.double)
		self.surface = np.empty((self.__nbFaces), dtype=np.double)

		# Copy node coordinates
		gNodes = mesh.geometry.nodes
		for rNodes in range(self.__nbNodes):
			self.X[rNodes] = gNodes[rNodes]

	"""
	 Job computeOutgoingFlux called @1.0 in executeTimeLoopN method.
	 In variables: V, center, deltat, surface, u_n
	 Out variables: outgoingFlux
	"""
	def _computeOutgoingFlux(self):
		for j1Cells in range(self.__nbCells):
			j1Id = j1Cells
			reduction0 = 0.0
			neighbourCellsJ1 = mesh.getNeighbourCells(j1Id)
			nbNeighbourCellsJ1 = len(neighbourCellsJ1)
			for j2NeighbourCellsJ1 in range(nbNeighbourCellsJ1):
				j2Id = neighbourCellsJ1[j2NeighbourCellsJ1]
				j2Cells = j2Id
				cfId = mesh.getCommonFace(j1Id, j2Id)
				cfFaces = cfId
				reduction1 = (self.u_n[j2Cells] - self.u_n[j1Cells]) / self.__norm(self.__operatorSub(self.center[j2Cells], self.center[j1Cells])) * self.surface[cfFaces]
				reduction0 = self.__sumR0(reduction0, reduction1)
			self.outgoingFlux[j1Cells] = self.deltat / self.V[j1Cells] * reduction0

	"""
	 Job computeSurface called @1.0 in simulate method.
	 In variables: X
	 Out variables: surface
	"""
	def _computeSurface(self):
		for fFaces in range(self.__nbFaces):
			fId = fFaces
			reduction0 = 0.0
			nodesOfFaceF = mesh.getNodesOfFace(fId)
			nbNodesOfFaceF = len(nodesOfFaceF)
			for rNodesOfFaceF in range(nbNodesOfFaceF):
				rId = nodesOfFaceF[rNodesOfFaceF]
				rPlus1Id = nodesOfFaceF[(rNodesOfFaceF+1+nbNodesOfFaceF)%nbNodesOfFaceF]
				rNodes = rId
				rPlus1Nodes = rPlus1Id
				reduction0 = self.__sumR0(reduction0, self.__norm(self.__operatorSub(self.X[rNodes], self.X[rPlus1Nodes])))
			self.surface[fFaces] = 0.5 * reduction0

	"""
	 Job computeTn called @1.0 in executeTimeLoopN method.
	 In variables: deltat, t_n
	 Out variables: t_nplus1
	"""
	def _computeTn(self):
		self.t_nplus1 = self.t_n + self.deltat

	"""
	 Job computeV called @1.0 in simulate method.
	 In variables: X
	 Out variables: V
	"""
	def _computeV(self):
		for jCells in range(self.__nbCells):
			jId = jCells
			reduction0 = 0.0
			nodesOfCellJ = mesh.getNodesOfCell(jId)
			nbNodesOfCellJ = len(nodesOfCellJ)
			for rNodesOfCellJ in range(nbNodesOfCellJ):
				rId = nodesOfCellJ[rNodesOfCellJ]
				rPlus1Id = nodesOfCellJ[(rNodesOfCellJ+1+nbNodesOfCellJ)%nbNodesOfCellJ]
				rNodes = rId
				rPlus1Nodes = rPlus1Id
				reduction0 = self.__sumR0(reduction0, self.__det(self.X[rNodes], self.X[rPlus1Nodes]))
			self.V[jCells] = 0.5 * reduction0

	"""
	 Job iniCenter called @1.0 in simulate method.
	 In variables: X
	 Out variables: center
	"""
	def _iniCenter(self):
		for jCells in range(self.__nbCells):
			jId = jCells
			reduction0 = np.full((2), 0.0, dtype=np.double)
			nodesOfCellJ = mesh.getNodesOfCell(jId)
			nbNodesOfCellJ = len(nodesOfCellJ)
			for rNodesOfCellJ in range(nbNodesOfCellJ):
				rId = nodesOfCellJ[rNodesOfCellJ]
				rNodes = rId
				reduction0 = self.__sumR1(reduction0, self.X[rNodes])
			self.center[jCells] = self.__operatorMult(0.25, reduction0)

	"""
	 Job iniF called @1.0 in simulate method.
	 In variables: 
	 Out variables: f
	"""
	def _iniF(self):
		for jCells in range(self.__nbCells):
			self.f[jCells] = 0.0

	"""
	 Job iniTime called @1.0 in simulate method.
	 In variables: 
	 Out variables: t_n0
	"""
	def _iniTime(self):
		self.t_n0 = 0.0

	"""
	 Job computeUn called @2.0 in executeTimeLoopN method.
	 In variables: deltat, f, outgoingFlux, u_n
	 Out variables: u_nplus1
	"""
	def _computeUn(self):
		for jCells in range(self.__nbCells):
			self.u_nplus1[jCells] = self.f[jCells] * self.deltat + self.u_n[jCells] + self.outgoingFlux[jCells]

	"""
	 Job iniUn called @2.0 in simulate method.
	 In variables: PI, alpha, center
	 Out variables: u_n
	"""
	def _iniUn(self):
		for jCells in range(self.__nbCells):
			self.u_n[jCells] = math.cos(2 * self.PI * self.alpha * self.center[jCells, 0])

	"""
	 Job setUpTimeLoopN called @2.0 in simulate method.
	 In variables: t_n0
	 Out variables: t_n
	"""
	def _setUpTimeLoopN(self):
		self.t_n = self.t_n0

	"""
	 Job executeTimeLoopN called @3.0 in simulate method.
	 In variables: lastDump, maxIterations, n, outputPeriod, stopTime, t_n, t_nplus1, u_n
	 Out variables: t_nplus1, u_nplus1
	"""
	def _executeTimeLoopN(self):
		self.n = 0
		self.t_n = 0.0
		continueLoop = True
		while continueLoop:
			self.n += 1
			print("START ITERATION n: %5d - t: %5.5f - deltat: %5.5f\n" % (self.n, self.t_n, self.deltat))
			if (self.n >= self.lastDump + self.outputPeriod):
				self.__dumpVariables(self.n)
		
			self._computeOutgoingFlux() # @1.0
			self._computeTn() # @1.0
			self._computeUn() # @2.0
		
			# Evaluate loop condition with variables at time n
			continueLoop = (self.t_nplus1 < self.stopTime  and  self.n + 1 < self.maxIterations)
		
			self.t_n = self.t_nplus1
			for i1Cells in range(self.__nbCells):
				self.u_n[i1Cells] = self.u_nplus1[i1Cells]
		
		print("FINAL TIME: %5.5f - deltat: %5.5f\n" % (self.t_n, self.deltat))
		self.__dumpVariables(self.n+1);

	def __det(self, a, b):
		return (a[0] * b[1] - a[1] * b[0])

	def __norm(self, a):
		return math.sqrt(self.__dot(a, a))

	def __dot(self, a, b):
		result = 0.0
		for i in range(len(a)):
			result = result + a[i] * b[i]
		return result

	def __sumR1(self, a, b):
		return self.__operatorAdd(a, b)

	def __sumR0(self, a, b):
		return a + b

	def __operatorAdd(self, a, b):
		result = np.empty((len(a)), dtype=np.double)
		for ix0 in range(len(a)):
			result[ix0] = a[ix0] + b[ix0]
		return result

	def __operatorMult(self, a, b):
		result = np.empty((len(b)), dtype=np.double)
		for ix0 in range(len(b)):
			result[ix0] = a * b[ix0]
		return result

	def __operatorSub(self, a, b):
		result = np.empty((len(a)), dtype=np.double)
		for ix0 in range(len(a)):
			result[ix0] = a[ix0] - b[ix0]
		return result

	def simulate(self):
		print("Start execution of heatEquation")
		self._computeSurface() # @1.0
		self._computeV() # @1.0
		self._iniCenter() # @1.0
		self._iniF() # @1.0
		self._iniTime() # @1.0
		self._iniUn() # @2.0
		self._setUpTimeLoopN() # @2.0
		self._executeTimeLoopN() # @3.0
		print("End of execution of heatEquation")

	def __dumpVariables(self, iteration):
		if not self.__writer.disabled:
			quads = mesh.geometry.quads
			self.__writer.startVtpFile(iteration, self.t_n, self.X, quads)
			self.__writer.openNodeData()
			self.__writer.closeNodeData()
			self.__writer.openCellData()
			self.__writer.openCellArray("Temperature", 0);
			for i in range(self.__nbCells):
				self.__writer.write(self.u_n[i])
			self.__writer.closeCellArray()
			self.__writer.closeCellData()
			self.__writer.closeVtpFile()
			self.lastDump = self.n

if __name__ == '__main__':
	args = sys.argv[1:]
	
	if len(args) == 1:
		f = open(args[0])
		data = json.load(f)
		f.close()

		# Mesh instanciation
		mesh = CartesianMesh2D()
		mesh.jsonInit(data["mesh"])

		# Module instanciation
		heatEquation = HeatEquation(mesh)
		heatEquation.jsonInit(data["heatEquation"])

		# Start simulation
		heatEquation.simulate()
	else:
		print("[ERROR] Wrong number of arguments: expected 1, actual " + str(len(args)), file=sys.stderr)
		print("        Expecting user data file name, for example HeatEquation.json", file=sys.stderr)
		exit(1)
