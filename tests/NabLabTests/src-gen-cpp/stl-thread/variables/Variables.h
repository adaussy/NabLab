/* DO NOT EDIT THIS FILE - it is machine generated */

#ifndef __VARIABLES_H_
#define __VARIABLES_H_

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
#include "nablalib/utils/stl/Parallel.h"
#include "CartesianMesh2D.h"

using namespace nablalib::utils;
using namespace nablalib::types;
using namespace nablalib::utils::stl;

/******************** Free functions declarations ********************/

namespace variablesfreefuncs
{
bool assertEquals(int expected, int actual);
template<size_t x>
bool assertEquals(RealArray1D<x> expected, RealArray1D<x> actual);
template<size_t x>
bool assertEquals(IntArray1D<x> expected, IntArray1D<x> actual);
template<size_t x0>
RealArray1D<x0> operator+(RealArray1D<x0> a, RealArray1D<x0> b);
}

/******************** Module declaration ********************/

class Variables
{
public:
	Variables(CartesianMesh2D& aMesh);
	~Variables();

	void jsonInit(const char* jsonContent);

	void simulate();
	void dynamicVecInitialization() noexcept;
	void varVecInitialization() noexcept;
	void oracle() noexcept;

private:
	// Mesh and mesh variables
	CartesianMesh2D& mesh;
	size_t nbNodes;

	// Options and global variables
	static constexpr double maxTime = 0.1;
	static constexpr int maxIter = 500;
	static constexpr double deltat = 1.0;
	static constexpr double t = 0.0;
	std::vector<RealArray1D<2>> X;
	int optDim;
	RealArray1D<2> optVect1;
	RealArray1D<2> optVect2;
	RealArray1D<2> optVect3;
	int mandatoryOptDim;
	IntArray1D<2> mandatoryOptVect;
	static constexpr int constexprDim = 2;
	static constexpr RealArray1D<constexprDim> constexprVec = {1.1, 1.1};
	RealArray1D<constexprDim> varVec;
	int checkDynamicDim;
	RealArray1D<0> dynamicVec;

	// Timers
	Timer globalTimer;
	Timer cpuTimer;
	Timer ioTimer;
};

#endif
