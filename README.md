# wb-handler

`wb-handler` is a small Clojure project to perform tasks on the WeeklyBeats.com RSS feed, such as populating a database with the data or only download the tracks from your favorite artists.

## Usage

Download your favorite artists tracks by calling `download-favorites-week` with week number and a coll of the artist names, which are case sensitivem like so: `(download-favorites-week i ["trash80" "Dos.Putin"])`. This rebinds the `*favorite-artists*` var, which will be used if you only call the function with a week number. See what weeks are available with `(available-weeks)`, and update the RSS feed by calling `(p/update-rss-feed!)`.

Default database is sqlite `database.db`, but this can be changed in `wb-handler.db`. Run `(create-db)` in `wb-handler.db` to make the sqlite database, and populate it with `(insert-week i)`.


## License

Copyright (C) 2012 Aleksander Skjæveland Larsen

Distributed under the Eclipse Public License, the same as Clojure.

All data from WeeklyBeats.com is (C) or some (CC) WeeklyBeats or the respective artists. Check description on a track for relevant copyright info.
