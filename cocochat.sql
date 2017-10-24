-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 24, 2017 at 06:45 PM
-- Server version: 10.1.26-MariaDB
-- PHP Version: 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cocochat`
--

-- --------------------------------------------------------

--
-- Table structure for table `conversations`
--

CREATE TABLE `conversations` (
  `PK_convo_ID` int(11) NOT NULL,
  `FK_usr_A` int(11) NOT NULL,
  `FK_usr_B` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `conversations`
--

INSERT INTO `conversations` (`PK_convo_ID`, `FK_usr_A`, `FK_usr_B`) VALUES
(1, 2, 1),
(2, 3, 1),
(3, 4, 1),
(4, 3, 2),
(5, 4, 2),
(6, 1, 5),
(7, 2, 5),
(8, 3, 5),
(9, 4, 5);

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `PK_FriendID` int(6) NOT NULL,
  `FK_Usr_id` int(6) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `friend_requests`
--

CREATE TABLE `friend_requests` (
  `PK_request_id` int(11) NOT NULL,
  `FK_sender_id` int(11) NOT NULL,
  `FK_target_id` int(11) NOT NULL,
  `req_state` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `friend_requests`
--

INSERT INTO `friend_requests` (`PK_request_id`, `FK_sender_id`, `FK_target_id`, `req_state`) VALUES
(1, 1, 2, 1),
(2, 5, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `PK_Group_ID` int(6) NOT NULL,
  `group_name` varchar(16) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `PK_msg_ID` int(11) NOT NULL,
  `FK_convo_ID` int(11) NOT NULL,
  `FK_usr_ID` int(11) NOT NULL,
  `msg_text` text COLLATE utf8_unicode_ci NOT NULL,
  `msg_type` text COLLATE utf8_unicode_ci NOT NULL,
  `msg_state` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`PK_msg_ID`, `FK_convo_ID`, `FK_usr_ID`, `msg_text`, `msg_type`, `msg_state`) VALUES
(1, 1, 1, 'Hey there!', 'user', 1),
(2, 1, 2, 'Oh no...', 'user', 1),
(3, 1, 1, '???', 'user', 1),
(4, 1, 1, 'Imma creep!', 'user', 1),
(5, 1, 1, 'Imma weirdo!', 'user', 1),
(6, 1, 2, 'Fuck off...', 'user', 1),
(7, 1, 1, 'Understandable', 'user', 1),
(8, 1, 2, 'Have a nice day...', 'user', 1),
(9, 1, 1, ':0!', 'user', 0),
(10, 1, 1, 'I can\'t believe you did it!', 'user', 0),
(11, 1, 1, 'You completed the meme! :0', 'user', 0),
(12, 1, 1, 'This is the best day EVER!', 'user', 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `PK_usr_ID` int(6) NOT NULL,
  `usr_Name` varchar(16) NOT NULL,
  `usr_password` varchar(16) NOT NULL,
  `usr_state` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`PK_usr_ID`, `usr_Name`, `usr_password`, `usr_state`) VALUES
(1, 'vrpg1998', '123456', 0),
(2, 'tyorke', '123456', 0),
(3, 'jteller', '123456', 0),
(4, 'adelarge', '123456', 0),
(5, 'asdf', '123456', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_friends`
--

CREATE TABLE `user_friends` (
  `PK_USRFRIEND_ID` int(6) NOT NULL,
  `FK_USR_ID` int(6) NOT NULL,
  `FK_Friend_id` int(6) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_friends`
--

INSERT INTO `user_friends` (`PK_USRFRIEND_ID`, `FK_USR_ID`, `FK_Friend_id`) VALUES
(1, 2, 1),
(2, 1, 2),
(3, 1, 5),
(4, 5, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_groups`
--

CREATE TABLE `user_groups` (
  `PK_UG_ID` int(6) NOT NULL,
  `FK_USR_ID` int(6) NOT NULL,
  `FK_GROUP_ID` int(6) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `conversations`
--
ALTER TABLE `conversations`
  ADD PRIMARY KEY (`PK_convo_ID`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`PK_FriendID`);

--
-- Indexes for table `friend_requests`
--
ALTER TABLE `friend_requests`
  ADD PRIMARY KEY (`PK_request_id`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`PK_Group_ID`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`PK_msg_ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`PK_usr_ID`);

--
-- Indexes for table `user_friends`
--
ALTER TABLE `user_friends`
  ADD PRIMARY KEY (`PK_USRFRIEND_ID`);

--
-- Indexes for table `user_groups`
--
ALTER TABLE `user_groups`
  ADD PRIMARY KEY (`PK_UG_ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `conversations`
--
ALTER TABLE `conversations`
  MODIFY `PK_convo_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `PK_FriendID` int(6) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `friend_requests`
--
ALTER TABLE `friend_requests`
  MODIFY `PK_request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `PK_Group_ID` int(6) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `PK_msg_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `PK_usr_ID` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user_friends`
--
ALTER TABLE `user_friends`
  MODIFY `PK_USRFRIEND_ID` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user_groups`
--
ALTER TABLE `user_groups`
  MODIFY `PK_UG_ID` int(6) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
