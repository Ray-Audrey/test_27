// The only file that differs between the two projects.
export default {
  "title": "Flights Management",
  "main": {
    "label": "Flights",
    "path": "/api/flights",
    "id": "fid",
    "fields": [
      {
        "name": "airline",
        "type": "text"
      },
      {
        "name": "source",
        "type": "text"
      },
      {
        "name": "destination",
        "type": "text"
      },
      {
        "name": "price",
        "type": "number"
      }
    ]
  },
  "second": {
    "label": "Scheduled Flights",
    "path": "/api/scheduled-flights",
    "id": "sid",
    "fields": [
      {
        "name": "flightId",
        "type": "ref"
      },
      {
        "name": "departureDate",
        "type": "date"
      },
      {
        "name": "status",
        "type": "select",
        "options": [
          "On-Time",
          "Delayed",
          "Cancelled"
        ]
      },
      {
        "name": "seatsAvailable",
        "type": "number"
      }
    ]
  },
  "reports": [
    {
      "label": "Delayed",
      "path": "/api/flights/delayed"
    },
    {
      "label": "> 50 seats",
      "path": "/api/flights/more-than-fifty-seats"
    },
    {
      "label": "Chennai schedules",
      "path": "/api/flights/chennai-schedules"
    },
    {
      "label": "Premium on-time",
      "path": "/api/flights/premium-on-time"
    },
    {
      "label": "Total seats",
      "path": "/api/flights/total-seats"
    },
    {
      "label": ">= 50 seats",
      "path": "/api/flights/at-least-fifty-seats"
    },
    {
      "label": "Sorted by price",
      "path": "/api/flights/sorted-by-price"
    }
  ]
}
