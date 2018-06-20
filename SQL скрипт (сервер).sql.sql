-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Хост: localhost:3306
-- Время создания: Июн 05 2018 г., 18:27
-- Версия сервера: 10.1.31-MariaDB
-- Версия PHP: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `id5750484_db`
--

-- --------------------------------------------------------

--
-- Структура таблицы `achievements`
--

CREATE TABLE `achievements` (
  `_id` int(11) NOT NULL,
  `status` varchar(26) COLLATE utf8_unicode_ci NOT NULL DEFAULT '00000000000000000000000000',
  `all_levels` int(11) NOT NULL DEFAULT '0',
  `all_money` int(11) NOT NULL DEFAULT '0',
  `all_eating` int(11) NOT NULL DEFAULT '0',
  `all_wins` int(11) NOT NULL DEFAULT '0',
  `id_user` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Дамп данных таблицы `achievements`
--

INSERT INTO `achievements` (`_id`, `status`, `all_levels`, `all_money`, `all_eating`, `all_wins`, `id_user`) VALUES
(27, '00000010001000100000010000', 3, 9, 24, 3, 48),
(29, '00000010001000100000010000', 5, 7, 25, 3, 69),
(30, '10000010001000100000010000', 10, 35, 40, 7, 70),
(31, '00000010001000111111110000', 4, 7, 15, 3, 71),
(32, '10000011101111111011011000', 272, 622, 874, 150, 72),
(33, '11111111111000111111111110', 70701, 37266521, 39, 60477, 73);

-- --------------------------------------------------------

--
-- Структура таблицы `resume`
--

CREATE TABLE `resume` (
  `_id` int(11) NOT NULL,
  `mode` int(11) NOT NULL DEFAULT '1',
  `score` int(11) NOT NULL DEFAULT '0',
  `all_rewards` int(11) NOT NULL DEFAULT '0',
  `score_1` int(11) DEFAULT '0',
  `all_rewards_1` int(11) DEFAULT '0',
  `score_2` int(11) DEFAULT '0',
  `all_rewards_2` int(11) DEFAULT '0',
  `score_3` int(11) DEFAULT '0',
  `all_rewards_3` int(11) DEFAULT '0',
  `score_4` int(11) DEFAULT '0',
  `all_rewards_4` int(11) DEFAULT '0',
  `id_user` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Дамп данных таблицы `resume`
--

INSERT INTO `resume` (`_id`, `mode`, `score`, `all_rewards`, `score_1`, `all_rewards_1`, `score_2`, `all_rewards_2`, `score_3`, `all_rewards_3`, `score_4`, `all_rewards_4`, `id_user`) VALUES
(27, 2, 5, 20, 0, 0, 0, 0, 5, 20, 9, 54, 48),
(29, 3, 2, 5, 0, 0, 0, 0, 0, 0, 0, 0, 69),
(30, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70),
(31, 2, 2, 5, 0, 0, 0, 0, 0, 0, 0, 0, 71),
(32, 1, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 72),
(33, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 73);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `_id` int(11) NOT NULL,
  `login` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `money` int(11) NOT NULL DEFAULT '0',
  `record` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`_id`, `login`, `password`, `email`, `money`, `record`) VALUES
(48, 'dmitry', '1111', 'dmitry@mail.ru', 29, 3),
(50, 'pinky', '1111', '1', 0, 5),
(51, 'hellboy', '1111', '2', 0, 52),
(52, 'helli', '1111', '3', 0, 51),
(53, 'willy', '1111', '4', 0, 15),
(54, 'maloy', '1111', '5', 0, 154),
(55, 'mania', '1111', '6', 0, 23),
(56, 'evgen', '1111', '7', 0, 47),
(57, 'katy', '1111', '8', 0, 149),
(58, 'happy', '1111', '9', 0, 87),
(59, 'sween', '1111', '10', 0, 5),
(60, 'pappy', '1111', '11', 0, 1),
(61, 'kitten', '1111', '12', 0, 0),
(62, 'sworms', '1111', '13', 0, 1),
(63, 'merry', '1111', '14', 0, 5),
(64, 'sikwel', '1111', '15', 0, 17),
(65, 'luk', '1111', '16', 0, 2),
(66, 'pillow', '1111', '17', 0, 34),
(67, 'barney', '1111', '18', 0, 23),
(68, 'marshall', '1111', '19', 0, 11),
(69, 'dima', '11111', 'dima@mail.ru', 27, 2),
(70, 'Irka', '1111', 'Irka@mail.ru', 60, 7),
(71, 'hellio', '1111', 'hell@mail.ru', 8527, 2),
(72, 'irina', '07071996', 'irina.yarkova.1996@mail.ru', 1817, 14),
(73, 'exilles', '1111', 'exilles@mail.ru', 37269641, 1594);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `achievements`
--
ALTER TABLE `achievements`
  ADD PRIMARY KEY (`_id`),
  ADD UNIQUE KEY `_id_UNIQUE` (`_id`),
  ADD KEY `id_user_idx` (`id_user`);

--
-- Индексы таблицы `resume`
--
ALTER TABLE `resume`
  ADD PRIMARY KEY (`_id`),
  ADD UNIQUE KEY `_id_UNIQUE` (`_id`),
  ADD KEY `id_user_idx` (`id_user`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`_id`),
  ADD UNIQUE KEY `idtable1_UNIQUE` (`_id`),
  ADD UNIQUE KEY `login_UNIQUE` (`login`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `achievements`
--
ALTER TABLE `achievements`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT для таблицы `resume`
--
ALTER TABLE `resume`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `achievements`
--
ALTER TABLE `achievements`
  ADD CONSTRAINT `achievementsUserFK` FOREIGN KEY (`id_user`) REFERENCES `users` (`_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `resume`
--
ALTER TABLE `resume`
  ADD CONSTRAINT `resumeUserFK` FOREIGN KEY (`id_user`) REFERENCES `users` (`_id`) ON DELETE CASCADE ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
