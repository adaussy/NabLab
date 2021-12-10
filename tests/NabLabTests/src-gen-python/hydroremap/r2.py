"""
DO NOT EDIT THIS FILE - it is machine generated
"""
import sys
import json
import numpy as np
from cartesianmesh2d import CartesianMesh2D

class R2:

	def __init__(self, mesh):
		self.__mesh = mesh
		self.__nbCells = mesh.nbCells

	def jsonInit(self, jsonContent):
		self.rv2 = np.empty((self.__nbCells), dtype=np.double)

	"""
	 Job rj1 called @3.0 in simulate method.
	 In variables: hv3
	 Out variables: rv2
	"""
	def _rj1(self):
		for cCells in range(self.__nbCells):
			self.rv2[cCells] = self._mainModule.hv3[cCells] * 2.0

	"""
	 Job rj2 called @4.0 in simulate method.
	 In variables: rv2
	 Out variables: hv6
	"""
	def _rj2(self):
		for cCells in range(self.__nbCells):
			self._mainModule.hv6[cCells] = self.rv2[cCells] * 3.0

	def set_mainModule(self, value):
		self._mainModule = value
		self._mainModule.set_r2(self)