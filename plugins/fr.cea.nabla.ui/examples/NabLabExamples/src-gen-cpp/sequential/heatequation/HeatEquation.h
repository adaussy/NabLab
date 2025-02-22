/* DO NOT EDIT THIS FILE - it is machine generated */

#ifndef __HEATEQUATION_H_
#define __HEATEQUATION_H_

#include <fstream>
#include <iomanip>
#include <type_traits>
#include <limits>
#include <utility>
#include <cmath>
#include <rapidjson/document.h>
#include "nablalib/utils/Utils.h"
#include "nablalib/utils/Timer.h"
#include "nablalib/types/Types.h"
#include "CartesianMesh2D.h"
#include "PvdFileWriter2D.h"

using namespace nablalib::utils;
using namespace nablalib::types;

/******************** Free functions declarations ********************/

namespace heatequationfreefuncs
{
double det(RealArray1D<2> a, RealArray1D<2> b);
template<size_t x>
double norm(RealArray1D<x> a);
template<size_t x>
double dot(RealArray1D<x> a, RealArray1D<x> b);
template<size_t x>
RealArray1D<x> sumR1(RealArray1D<x> a, RealArray1D<x> b);
double sumR0(double a, double b);
template<size_t x0>
RealArray1D<x0> operator+(RealArray1D<x0> a, RealArray1D<x0> b);
template<size_t x0>
RealArray1D<x0> operator*(double a, RealArray1D<x0> b);
template<size_t x0>
RealArray1D<x0> operator-(RealArray1D<x0> a, RealArray1D<x0> b);
}

/******************** Module declaration ********************/

class HeatEquation
{
public:
	HeatEquation(CartesianMesh2D& aMesh);
	~HeatEquation();

	void jsonInit(const char* jsonContent);

	void simulate();
	void computeOutgoingFlux() noexcept;
	void computeSurface() noexcept;
	void computeTn() noexcept;
	void computeV() noexcept;
	void iniCenter() noexcept;
	void iniF() noexcept;
	void iniTime() noexcept;
	void computeUn() noexcept;
	void iniUn() noexcept;
	void setUpTimeLoopN() noexcept;
	void executeTimeLoopN() noexcept;

private:
	void dumpVariables(int iteration, bool useTimer=true);

	// Mesh and mesh variables
	CartesianMesh2D& mesh;
	size_t nbNodes, nbCells, nbFaces, maxNodesOfCell, maxNodesOfFace, maxNeighbourCells;

	// Options and global variables
	PvdFileWriter2D* writer;
	std::string outputPath;
	int outputPeriod;
	int lastDump;
	int n;
	double stopTime;
	int maxIterations;
	double PI;
	double alpha;
	static constexpr double deltat = 0.001;
	double t_n;
	double t_nplus1;
	double t_n0;
	std::vector<RealArray1D<2>> X;
	std::vector<RealArray1D<2>> center;
	std::vector<double> u_n;
	std::vector<double> u_nplus1;
	std::vector<double> V;
	std::vector<double> f;
	std::vector<double> outgoingFlux;
	std::vector<double> surface;

	// Timers
	Timer globalTimer;
	Timer cpuTimer;
	Timer ioTimer;
};

#endif
