# wb-handler

`wb-handler` is a small Clojure project to perform tasks on the WeeklyBeats.com RSS feed, such as populating a database with the data or only download the tracks from your favorite artists.

## Usage

Default database is sqlite `database.db`, but this can be changed in `wb-handler.db`. Run `(create-db)` in `wb-handler.db` to make the sqlite database, and populate it with `(insert-week i)` in `wb-handler.core`.



## License

Copyright (C) 2012 Aleksander Skjæveland Larsen

Distributed under the Eclipse Public License, the same as Clojure.

All data from WeeklyBeats.com is (C) or some (CC) WeeklyBeats or the respective artists. Check description on a track for relevant copyright info.
