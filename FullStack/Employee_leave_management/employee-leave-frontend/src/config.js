// The only file that differs between the two projects.
export default {
  "title": "Employee Leave Management",
  "main": {
    "label": "Employees",
    "path": "/api/employees",
    "id": "eid",
    "fields": [
      {
        "name": "name",
        "type": "text"
      },
      {
        "name": "department",
        "type": "text"
      },
      {
        "name": "salary",
        "type": "number"
      }
    ]
  },
  "second": {
    "label": "Leaves",
    "path": "/api/leaves",
    "id": "lid",
    "fields": [
      {
        "name": "employeeId",
        "type": "ref"
      },
      {
        "name": "leaveType",
        "type": "select",
        "options": [
          "Sick",
          "Casual",
          "Earned"
        ]
      },
      {
        "name": "days",
        "type": "number"
      }
    ]
  },
  "reports": [
    {
      "label": "Sick leave",
      "path": "/api/employees/sick-leave"
    },
    {
      "label": "Long leave",
      "path": "/api/employees/long-leave"
    },
    {
      "label": "CSE leave",
      "path": "/api/employees/cse-leave"
    },
    {
      "label": "High salary + casual",
      "path": "/api/employees/high-salary-casual-leave"
    },
    {
      "label": "Total leave days",
      "path": "/api/employees/total-leave-days"
    },
    {
      "label": "3+ days",
      "path": "/api/employees/at-least-three-days"
    },
    {
      "label": "Sorted by salary",
      "path": "/api/employees/sorted-by-salary"
    }
  ]
}
