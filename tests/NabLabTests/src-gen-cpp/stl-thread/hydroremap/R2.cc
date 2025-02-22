/* DO NOT EDIT THIS FILE - it is machine generated */

#include "R2.h"
#include <rapidjson/document.h>
#include <rapidjson/istreamwrapper.h>
#include <rapidjson/stringbuffer.h>
#include <rapidjson/writer.h>


/******************** Module definition ********************/

R2::R2(CartesianMesh2D& aMesh)
: mesh(aMesh)
, nbNodes(mesh.getNbNodes())
, nbCells(mesh.getNbCells())
, rv2(nbCells)
{
}

R2::~R2()
{
}

void
R2::jsonInit(const char* jsonContent)
{
	rapidjson::Document document;
	assert(!document.Parse(jsonContent).HasParseError());
	assert(document.IsObject());
	const rapidjson::Value::Object& options = document.GetObject();

}


/**
 * Job rj1 called @3.0 in simulate method.
 * In variables: hv3
 * Out variables: rv2
 */
void R2::rj1() noexcept
{
	parallel_exec(nbCells, [&](const size_t& cCells)
	{
		rv2[cCells] = mainModule->hv3[cCells] * 2.0;
	});
}

/**
 * Job rj2 called @4.0 in simulate method.
 * In variables: rv2
 * Out variables: hv6
 */
void R2::rj2() noexcept
{
	parallel_exec(nbCells, [&](const size_t& cCells)
	{
		mainModule->hv6[cCells] = rv2[cCells] * 3.0;
	});
}
