package com.khater.rwaq.presentation.util

const val EXAMPLE_POINT_GEO_JSON =
    """
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "title": "Random 1"
      },
      "geometry": {
        "coordinates": [
          19.912189144770718,
          50.06865411437797
        ],
        "type": "Point"
      },
      "id": 0
    },
    {
      "type": "Feature",
      "properties": {
        "title": "Random 2"
      },
      "geometry": {
        "coordinates": [
          19.912786665903937,
          50.06756184910992
        ],
        "type": "Point"
      },
      "id": 1
    },
    {
      "type": "Feature",
      "properties": {
        "title": "Random 3"
      },
      "geometry": {
        "coordinates": [
          19.916507529421125,
          50.07029941818287
        ],
        "type": "Point"
      },
      "id": 2
    },
    {
      "type": "Feature",
      "properties": {
        "title": "Random 4"
      },
      "geometry": {
        "coordinates": [
          19.9137020847733,
          50.07131293218115
        ],
        "type": "Point"
      },
      "id": 3
    }
  ]
}
    """

const val EXAMPLE_POLYGON_GEO_JSON =
    """
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "name": "Office area",
        "stroke": "#0000FF",
        "stroke-width": 6,
        "fill": "#0000FF",
        "fill-opacity": 0.25
      },
      "geometry": {
        "coordinates": [
          [
            [
              19.96499693844865,
              50.048781853657715
            ],
            [
              19.964950203807007,
              50.048641806985216
            ],
            [
              19.96617049725603,
              50.04850509436312
            ],
            [
              19.966321086660287,
              50.04870182703783
            ],
            [
              19.965204647972712,
              50.04883853909956
            ],
            [
              19.965163106067905,
              50.04876518145625
            ],
            [
              19.96499693844865,
              50.048781853657715
            ]
          ]
        ],
        "type": "Polygon"
      }
    },
    {
      "type": "Feature",
      "properties": {
        "name": "Office area",
        "stroke": "#0000FF",
        "stroke-width": 6,
        "fill": "#0000FF",
        "fill-opacity": 0.25
      },
      "geometry": {
        "coordinates": [
          [
            [
              19.965568139637867,
              50.04929202022299
            ],
            [
              19.965521404995116,
              50.04903860482935
            ],
            [
              19.966419748683762,
              50.04893190321056
            ],
            [
              19.966471676064486,
              50.049115296471314
            ],
            [
              19.965568139637867,
              50.04929202022299
            ]
          ]
        ],
        "type": "Polygon"
      }
    }
  ]
}
"""

const val EXAMPLE_LINE_GEO_JSON =
    """
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "stroke-width": 10,
        "stroke": "#FF0000",
        "name": "Route to the office"
      },
      "geometry": {
        "coordinates": [
          [
            19.964725868477615,
            50.04984119906527
          ],
          [
            19.964562032014555,
            50.04962734365236
          ],
          [
            19.96552893573039,
            50.049382443541276
          ],
          [
            19.9653812143288,
            50.04890471226287
          ]
        ],
        "type": "LineString"
      },
      "id": 0
    }
  ]
}
"""