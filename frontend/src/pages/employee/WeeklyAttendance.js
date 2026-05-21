// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { Bar } from 'react-chartjs-2';
// import { Chart, registerables } from 'chart.js';
// import {
//     Container,
//     Form,
//     Button,
//     Row,
//     Col,
//     Card,
// } from 'react-bootstrap';

// Chart.register(...registerables);

// const WeeklyAttendance = () => {
//     const [employeeId, setEmployeeId] = useState(null);
//     const [startDate, setStartDate] = useState('');
//     const [endDate, setEndDate] = useState('');
//     const [attendanceData, setAttendanceData] = useState([]);

//     useEffect(() => {
//         const getEmpIdFromToken = () => {
//             const token = localStorage.getItem('token');
//             if (token) {
//                 try {
//                     const decodedToken = JSON.parse(atob(token.split('.')[1]));
//                     return decodedToken.userId;
//                 } catch (error) {
//                     console.error('Invalid token:', error);
//                 }
//             }
//             return null;
//         };
//         setEmployeeId(getEmpIdFromToken());
//     }, []);

//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         if (!startDate || !endDate || !employeeId) return;

//         setAttendanceData([]); // Clear previous data

//         try {
//             const response = await axios.get(
//                 `http://localhost:8765/api/attendance/${employeeId}?start=${startDate}&end=${endDate}`,
//                 {
//                     headers: {
//                         Authorization: `Bearer ${localStorage.getItem('token')}`,
//                     },
//                 }
//             );
//             setAttendanceData(response.data);
//         } catch (error) {
//             console.error('Error fetching attendance data:', error);
//         }
//     };

//     const calculateTimeDifference = (checkIn, checkOut) => {
//         if (!checkIn || !checkOut) {
//             return 'N/A';
//         }
//         const inTime = new Date(checkIn);
//         const outTime = new Date(checkOut);
//         const diffInMilliseconds = outTime.getTime() - inTime.getTime();
//         const diffInHours = diffInMilliseconds / (1000 * 60 * 60);
//         const hours = Math.floor(diffInHours);
//         const minutes = Math.floor((diffInHours - hours) * 60);
//         return `${hours}h ${minutes}m`;
//     };

//     const chartData = {
//         labels: attendanceData.map((entry) => entry.date),
//         datasets: [
//             {
//                 label: 'Attendance',
//                 data: attendanceData.map((entry) => {
//                     if (entry.status === 'PRESENT') {
//                         return [0, 1]; 
//                     } else if (entry.status === 'EARLY_LEAVE') {
//                         return [0, 0.5]; 
//                     } else {
//                         return [0, 0.1]; 
//                     }
//                 }),
//                 backgroundColor: attendanceData.map((entry) => {
//                     if (entry.status === 'PRESENT') {
//                         return '#4caf50';
//                     } else if (entry.status === 'EARLY_LEAVE') {
//                         return '#ffc107'; // Show black color for early leave
//                     } else {
//                         return '#f44336';
//                     }
//                 }),
//                 borderWidth: 1,
//             },
//         ],
//     };

//     const chartOptions = {
//         scales: {
//             y: {
//                 display: false, // Hide Y-axis
//                 beginAtZero: true,
//                 max: 1,
//             },
//             x: {
//                 title: {
//                     display: true,
//                     text: 'Date',
//                 },
//             },
//         },
//         elements: {
//             bar: {
//                 // Make bars start at the beginning of the day
//                 base: 0,
//                 inflateAmount: 0,
//                 // Use a function to dynamically set the bar thickness
//                 barThickness: 20, // Set a fixed thickness for all bars
//                 minBarLength: 0,
//             },
//         },
//         plugins: {
//             tooltip: {
//                 callbacks: {
//                     label: function (context) {
//                         const index = context.dataIndex;
//                         const entry = attendanceData[index];
//                         if (entry.status === 'PRESENT') {
//                             const inTime = entry.checkInTime?.substring(11, 16) || '-';
//                             const outTime = entry.checkOutTime?.substring(11, 16) || '-';
//                             const duration = calculateTimeDifference(entry.checkInTime, entry.checkOutTime);
//                             return `Present: In - ${inTime} | Out - ${outTime} | Duration - ${duration}`;
//                         } else if (entry.status === 'EARLY_LEAVE') {
//                             const inTime = entry.checkInTime?.substring(11, 16) || '-';
//                             const outTime = entry.checkOutTime?.substring(11, 16) || '-';
//                             const duration = calculateTimeDifference(entry.checkInTime, entry.checkOutTime);
//                             return `Early-Leave: In - ${inTime} | Out - ${outTime} | Duration - ${duration}`;
//                         } else {
//                             return 'Absent';
//                         }
//                     },
//                 },
//             },
//             legend: {
//                 display: false,
//             },
//         },
//         responsive: true,
//         maintainAspectRatio: false,
//     };

//     return (
//         <Container className="mt-5">
//             <Card className="shadow p-4">
//                 <h4 className="text-center mb-4">Weekly Attendance History</h4>
//                 <Form onSubmit={handleSubmit}>
//                     <Row className="mb-3">
//                         <Col md={5}>
//                             <Form.Group>
//                                 <Form.Label>From Date</Form.Label>
//                                 <Form.Control
//                                     type="date"
//                                     value={startDate}
//                                     onChange={(e) => setStartDate(e.target.value)}
//                                     required
//                                 />
//                             </Form.Group>
//                         </Col>
//                         <Col md={5}>
//                             <Form.Group>
//                                 <Form.Label>To Date</Form.Label>
//                                 <Form.Control
//                                     type="date"
//                                     value={endDate}
//                                     onChange={(e) => setEndDate(e.target.value)}
//                                     required
//                                 />
//                             </Form.Group>
//                         </Col>
//                         <Col md={2} className="d-flex align-items-end">
//                             <Button type="submit" className="w-100">
//                                 Show
//                             </Button>
//                         </Col>
//                     </Row>
//                 </Form>

//                 {attendanceData.length > 0 ? (
//                     <div style={{ height: '400px' }}>
//                         <Bar data={chartData} options={chartOptions} />
//                     </div>
//                 ) : (
//                     <p className="text-muted text-center mt-4">
//                         No attendance data found for selected range.
//                     </p>
//                 )}
//             </Card>
//         </Container>
//     );
// };

// export default WeeklyAttendance;


import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Bar } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';
import {
    Container,
    Form,
    Button,
    Row,
    Col,
    Card,
} from 'react-bootstrap';

Chart.register(...registerables);

const WeeklyAttendance = () => {
    const [employeeId, setEmployeeId] = useState(null);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [attendanceData, setAttendanceData] = useState([]);

    useEffect(() => {
        const getEmpIdFromToken = () => {
            const token = localStorage.getItem('token');
            if (token) {
                try {
                    const decodedToken = JSON.parse(atob(token.split('.')[1]));
                    return decodedToken.userId;
                } catch (error) {
                    console.error('Invalid token:', error);
                }
            }
            return null;
        };
        setEmployeeId(getEmpIdFromToken());
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!startDate || !endDate || !employeeId) return;

        setAttendanceData([]); // Clear previous data

        try {
            const response = await axios.get(
                `http://localhost:8765/api/attendance/${employeeId}?start=${startDate}&end=${endDate}`,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                }
            );
            setAttendanceData(response.data);
        } catch (error) {
            console.error('Error fetching attendance data:', error);
        }
    };

    const calculateTimeDifference = (checkIn, checkOut) => {
        if (!checkIn || !checkOut) {
            return 'N/A';
        }
        const inTime = new Date(checkIn);
        const outTime = new Date(checkOut);
        const diffInMilliseconds = outTime.getTime() - inTime.getTime();
        const diffInHours = diffInMilliseconds / (1000 * 60 * 60);
        const hours = Math.floor(diffInHours);
        const minutes = Math.floor((diffInHours - hours) * 60);
        return `${hours}h ${minutes}m`;
    };

    const today = new Date();
    const filteredAttendanceData = attendanceData.filter(entry => new Date(entry.date) <= today);

    const chartData = {
        labels: filteredAttendanceData.map((entry) => entry.date),
        datasets: [
            {
                label: 'Attendance',
                data: filteredAttendanceData.map((entry) => {
                    if (entry.status === 'PRESENT') {
                        return [0, 1];
                    } else if (entry.status === 'EARLY_LEAVE') {
                        return [0, 0.5];
                    } else {
                        return [0, 0.1];
                    }
                }),
                backgroundColor: filteredAttendanceData.map((entry) => {
                    if (entry.status === 'PRESENT') {
                        return '#4caf50';
                    } else if (entry.status === 'EARLY_LEAVE') {
                        return '#ffc107';
                    } else {
                        return '#f44336';
                    }
                }),
                borderWidth: 1,
            },
        ],
    };

    const chartOptions = {
        scales: {
            y: {
                display: false, // Hide Y-axis
                beginAtZero: true,
                max: 1,
            },
            x: {
                title: {
                    display: true,
                    text: 'Date',
                },
            },
        },
        elements: {
            bar: {
                base: 0,
                inflateAmount: 0,
                barThickness: 20,
                minBarLength: 0,
            },
        },
        plugins: {
            tooltip: {
                callbacks: {
                    label: function (context) {
                        const index = context.dataIndex;
                        const entry = filteredAttendanceData[index];
                        if (entry.status === 'PRESENT') {
                            const inTime = entry.checkInTime?.substring(11, 16) || '-';
                            const outTime = entry.checkOutTime?.substring(11, 16) || '-';
                            const duration = calculateTimeDifference(entry.checkInTime, entry.checkOutTime);
                            return `Present: In - ${inTime} | Out - ${outTime} | Duration - ${duration}`;
                        } else if (entry.status === 'EARLY_LEAVE') {
                            const inTime = entry.checkInTime?.substring(11, 16) || '-';
                            const outTime = entry.checkOutTime?.substring(11, 16) || '-';
                            const duration = calculateTimeDifference(entry.checkInTime, entry.checkOutTime);
                            return `Early-Leave: In - ${inTime} | Out - ${outTime} | Duration - ${duration}`;
                        } else {
                            return 'Absent';
                        }
                    },
                },
            },
            legend: {
                display: false,
            },
        },
        responsive: true,
        maintainAspectRatio: false,
    };

    return (
        <Container className="mt-5">
            <Card className="shadow p-4">
                <h4 className="text-center mb-4">Weekly Attendance History</h4>
                <Form onSubmit={handleSubmit}>
                    <Row className="mb-3">
                        <Col md={5}>
                            <Form.Group>
                                <Form.Label>From Date</Form.Label>
                                <Form.Control
                                    type="date"
                                    value={startDate}
                                    onChange={(e) => setStartDate(e.target.value)}
                                    required
                                />
                            </Form.Group>
                        </Col>
                        <Col md={5}>
                            <Form.Group>
                                <Form.Label>To Date</Form.Label>
                                <Form.Control
                                    type="date"
                                    value={endDate}
                                    onChange={(e) => setEndDate(e.target.value)}
                                    required
                                />
                            </Form.Group>
                        </Col>
                        <Col md={2} className="d-flex align-items-end">
                            <Button type="submit" className="w-100">
                                Show
                            </Button>
                        </Col>
                    </Row>
                </Form>

                {filteredAttendanceData.length > 0 ? (
                    <div style={{ height: '400px' }}>
                        <Bar data={chartData} options={chartOptions} />
                    </div>
                ) : (
                    <p className="text-muted text-center mt-4">
                        No attendance data found for selected range.
                    </p>
                )}
            </Card>
        </Container>
    );
};

export default WeeklyAttendance;