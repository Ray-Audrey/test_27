// The only file that differs between the two projects.
export default {
  "title": "Student Course Management",
  "main": {
    "label": "Students",
    "path": "/api/students",
    "id": "sid",
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
        "name": "cgpa",
        "type": "number"
      }
    ]
  },
  "second": {
    "label": "Courses Enrolled",
    "path": "/api/courses-enrolled",
    "id": "ceid",
    "fields": [
      {
        "name": "studentId",
        "type": "ref"
      },
      {
        "name": "courseName",
        "type": "text"
      },
      {
        "name": "credits",
        "type": "number"
      },
      {
        "name": "status",
        "type": "select",
        "options": [
          "Ongoing",
          "Completed",
          "Dropped"
        ]
      }
    ]
  },
  "reports": [
    {
      "label": "Dropped",
      "path": "/api/students/dropped"
    },
    {
      "label": "> 3 credits",
      "path": "/api/students/more-than-three-credits"
    },
    {
      "label": "CSE courses",
      "path": "/api/students/cse-courses"
    },
    {
      "label": "Topper + completed",
      "path": "/api/students/topper-completed"
    },
    {
      "label": "Total credits",
      "path": "/api/students/total-credits"
    },
    {
      "label": ">= 3 credits",
      "path": "/api/students/at-least-three-credits"
    },
    {
      "label": "Sorted by CGPA",
      "path": "/api/students/sorted-by-cgpa"
    }
  ]
}
