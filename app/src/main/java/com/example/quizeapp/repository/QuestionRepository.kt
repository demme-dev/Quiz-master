package com.example.quizeapp.repository

import com.example.quizeapp.model.Category
import com.example.quizeapp.model.Question

/**
 * Repository providing static quiz questions organized by category (15 per category).
 */
object QuestionRepository {

    /**
     * Returns questions filtered by the specified [Category].
     */
    fun getQuestionsForCategory(category: Category): List<Question> {
        return getAllQuestions().filter { it.category == category }
    }

    /**
     * Returns all available questions across all categories.
     */
    fun getQuestions(): List<Question> {
        return getAllQuestions()
    }

    /**
     * Complete collection of questions (15 per category).
     */
    private fun getAllQuestions(): List<Question> {
        return listOf(
            // --- COMPUTER SCIENCE (15 Questions) ---
            Question(
                text = "What does CPU stand for in computer systems?",
                options = listOf(
                    "Central Processing Unit",
                    "Computer Personal Unit",
                    "Central Process Unit",
                    "Central Peripheral Unit"
                ),
                correctIndex = 0,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "Which programming language is officially recommended by Google for Android development?",
                options = listOf(
                    "Swift",
                    "Kotlin",
                    "C#",
                    "Ruby"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What is the primary function of RAM in a computer?",
                options = listOf(
                    "Long-term file storage",
                    "Temporary high-speed memory for active data",
                    "Processing 3D graphics",
                    "Managing power distribution"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What does HTTP stand for in web technology?",
                options = listOf(
                    "HyperText Transfer Protocol",
                    "HighText Transfer Protocol",
                    "HyperText Test Program",
                    "Hyperlink Transfer Protocol"
                ),
                correctIndex = 0,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "In Object-Oriented Programming, what is Encapsulation?",
                options = listOf(
                    "Converting code to machine instructions",
                    "Bundling data and methods into a single unit",
                    "Creating multiple instances of a class",
                    "Inheriting attributes from a base class"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "Which data structure operates on a First-In, First-Out (FIFO) basis?",
                options = listOf(
                    "Stack",
                    "Queue",
                    "Binary Tree",
                    "Graph"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What year was the World Wide Web invented by Tim Berners-Lee?",
                options = listOf(
                    "1975",
                    "1989",
                    "1995",
                    "2001"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What binary number represents the decimal value 5?",
                options = listOf(
                    "100",
                    "101",
                    "110",
                    "111"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "Which database SQL command is used to retrieve data from a table?",
                options = listOf(
                    "UPDATE",
                    "INSERT",
                    "SELECT",
                    "DELETE"
                ),
                correctIndex = 2,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What does Git primarily manage in software development?",
                options = listOf(
                    "Network security",
                    "Source code version control",
                    "Database backups",
                    "User authentication"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "Which protocol is used for securely transferring files over SSH?",
                options = listOf(
                    "SFTP",
                    "HTTP",
                    "FTP",
                    "SMTP"
                ),
                correctIndex = 0,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What design pattern guarantees a class has only one instance?",
                options = listOf(
                    "Factory",
                    "Singleton",
                    "Observer",
                    "Adapter"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "Which notation describes the worst-case runtime complexity of an algorithm?",
                options = listOf(
                    "Big O notation",
                    "Big Theta notation",
                    "Big Omega notation",
                    "Little o notation"
                ),
                correctIndex = 0,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "What is the main purpose of an OS kernel?",
                options = listOf(
                    "Rendering user interfaces",
                    "Managing system hardware resources and software interaction",
                    "Hosting web servers",
                    "Formatting hard drives"
                ),
                correctIndex = 1,
                category = Category.COMPUTER_SCIENCE
            ),
            Question(
                text = "Which component compiles Kotlin code into bytecode targeting the JVM?",
                options = listOf(
                    "kotlinc",
                    "javac",
                    "gcc",
                    "lldb"
                ),
                correctIndex = 0,
                category = Category.COMPUTER_SCIENCE
            ),

            // --- GENERAL KNOWLEDGE (15 Questions) ---
            Question(
                text = "Which planet in our solar system is commonly known as the Red Planet?",
                options = listOf(
                    "Venus",
                    "Mars",
                    "Jupiter",
                    "Saturn"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "What is the chemical formula for pure water?",
                options = listOf(
                    "HO2",
                    "H2O",
                    "CO2",
                    "NaCl"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Which country is home to the Great Barrier Reef?",
                options = listOf(
                    "Brazil",
                    "Australia",
                    "Thailand",
                    "Mexico"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Who painted the Mona Lisa?",
                options = listOf(
                    "Vincent van Gogh",
                    "Pablo Picasso",
                    "Leonardo da Vinci",
                    "Claude Monet"
                ),
                correctIndex = 2,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "What is the largest ocean on Earth?",
                options = listOf(
                    "Atlantic Ocean",
                    "Indian Ocean",
                    "Arctic Ocean",
                    "Pacific Ocean"
                ),
                correctIndex = 3,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "What is the capital city of Japan?",
                options = listOf(
                    "Kyoto",
                    "Osaka",
                    "Tokyo",
                    "Hiroshima"
                ),
                correctIndex = 2,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "How many continents are there on Earth?",
                options = listOf(
                    "5",
                    "6",
                    "7",
                    "8"
                ),
                correctIndex = 2,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Which element has the chemical symbol 'Au'?",
                options = listOf(
                    "Silver",
                    "Gold",
                    "Copper",
                    "Aluminum"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "In which year did the Apollo 11 moon landing take place?",
                options = listOf(
                    "1965",
                    "1969",
                    "1972",
                    "1975"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Which gas do plants absorb from the atmosphere during photosynthesis?",
                options = listOf(
                    "Oxygen",
                    "Carbon Dioxide",
                    "Nitrogen",
                    "Hydrogen"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "What is the hardest natural substance on Earth?",
                options = listOf(
                    "Gold",
                    "Iron",
                    "Diamond",
                    "Quartz"
                ),
                correctIndex = 2,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Which country gifted the Statue of Liberty to the United States?",
                options = listOf(
                    "United Kingdom",
                    "France",
                    "Spain",
                    "Germany"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "What is the smallest country in the world by land area?",
                options = listOf(
                    "Monaco",
                    "Vatican City",
                    "San Marino",
                    "Liechtenstein"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Who wrote the play 'Romeo and Juliet'?",
                options = listOf(
                    "Charles Dickens",
                    "William Shakespeare",
                    "Mark Twain",
                    "Jane Austen"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),
            Question(
                text = "Which instrument measures atmospheric pressure?",
                options = listOf(
                    "Thermometer",
                    "Barometer",
                    "Altimeter",
                    "Hygrometer"
                ),
                correctIndex = 1,
                category = Category.GENERAL_KNOWLEDGE
            ),

            // --- MATHEMATICS (15 Questions) ---
            Question(
                text = "What is the value of Pi (π) rounded to two decimal places?",
                options = listOf(
                    "3.14",
                    "3.16",
                    "3.12",
                    "3.18"
                ),
                correctIndex = 0,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the square root of 144?",
                options = listOf(
                    "10",
                    "11",
                    "12",
                    "14"
                ),
                correctIndex = 2,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "In geometry, what is the sum of interior angles in a triangle?",
                options = listOf(
                    "90 degrees",
                    "180 degrees",
                    "270 degrees",
                    "360 degrees"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is 7 multiplied by 8?",
                options = listOf(
                    "54",
                    "56",
                    "64",
                    "48"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the perimeter of a square with a side length of 6 cm?",
                options = listOf(
                    "12 cm",
                    "18 cm",
                    "24 cm",
                    "36 cm"
                ),
                correctIndex = 2,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the prime number immediately following 11?",
                options = listOf(
                    "12",
                    "13",
                    "15",
                    "17"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is 15% of 200?",
                options = listOf(
                    "20",
                    "25",
                    "30",
                    "35"
                ),
                correctIndex = 2,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is 2 raised to the power of 5 (2^5)?",
                options = listOf(
                    "16",
                    "32",
                    "64",
                    "128"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the median of the numbers: 3, 7, 9, 12, 15?",
                options = listOf(
                    "7",
                    "9",
                    "10",
                    "12"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the result of 100 divided by 4?",
                options = listOf(
                    "20",
                    "25",
                    "30",
                    "50"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the factorial of 4 (4!)?",
                options = listOf(
                    "12",
                    "24",
                    "16",
                    "48"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "In a right-angled triangle, what is the side opposite to the right angle called?",
                options = listOf(
                    "Adjacent",
                    "Hypotenuse",
                    "Opposite",
                    "Radius"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is 11 squared (11^2)?",
                options = listOf(
                    "111",
                    "121",
                    "131",
                    "144"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "What is the mean of the numbers: 4, 8, 12, 16?",
                options = listOf(
                    "8",
                    "10",
                    "12",
                    "14"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            ),
            Question(
                text = "Solve for x: 3x - 5 = 10.",
                options = listOf(
                    "3",
                    "5",
                    "15",
                    "8"
                ),
                correctIndex = 1,
                category = Category.MATHEMATICS
            )
        )
    }
}
