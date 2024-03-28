package com.jowney.adhdgames.mainui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val ticTacToeIconString = "Noughts and crosses icons created by lutfix - Flaticon"
private const val ticTacToeIconUrl = "https://www.flaticon.com/free-icons/noughts-and-crosses"

private const val hangmanIconString = "Hangman icons created by Febrian Hidayat - Flaticon"
private const val hangmanIconUrl = "https://www.flaticon.com/free-icons/hangman"

private const val rockPaperScissorsIconString = "Rock paper scissors icons created by Cap Cool - Flaticon"
private const val rockPaperScissorsIconUrl = "https://www.flaticon.com/free-icons/rock-paper-scissors"

private const val dotsAndBoxesIconString = "Measurements icons created by Zlatko - Flaticon"
private const val dotsAndBoxesIconUrl = "https://www.flaticon.com/free-icons/measurements"

private const
val aboutString = "This app was built by Jonathan Owney\n\n" +
        "I made this for my wonderful family and friends who all have some form of ADD or ADHD\n\n" +
        "Credits:\n" +
        "Tabitha Tallent - QA testing, UI design, and just putting up with my bad code\n\n" +
        "Jonathan Tallent, Charles Tallent, and Tamra Tallent - QA testing and game ideas\n\n" +
        "Icon attribution:\n"

private val aboutFontSize = 18.sp

@Composable
fun AboutMain() {
    val uriHandler = LocalUriHandler.current
    val aboutTextStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = aboutFontSize,
        textDecoration = TextDecoration.Underline
    )

    Background {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = aboutString,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = aboutFontSize,
            )
            ClickableAboutText(
                aboutString = ticTacToeIconString,
                url = ticTacToeIconUrl,
                uriHandler = uriHandler,
                style = aboutTextStyle
            )
            ClickableAboutText(
                aboutString = hangmanIconString,
                url = hangmanIconUrl,
                uriHandler = uriHandler,
                style = aboutTextStyle
            )
            ClickableAboutText(
                aboutString = rockPaperScissorsIconString,
                url = rockPaperScissorsIconUrl,
                uriHandler = uriHandler,
                style = aboutTextStyle
            )
            ClickableAboutText(
                aboutString = dotsAndBoxesIconString,
                url = dotsAndBoxesIconUrl,
                uriHandler = uriHandler,
                style = aboutTextStyle
            )
        }
    }
}

@Composable
fun ClickableAboutText(aboutString: String, url: String, uriHandler: UriHandler, style: SpanStyle) {
    val clickableString = buildAnnotatedString {
        val str = aboutString
        append(str)

        addStyle(
            style = style,
            start = 0,
            end = str.length
        )

        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = 0,
            end = str.length
        )
    }

    return ClickableText(text = clickableString) { offset ->
        clickableString
            .getStringAnnotations(tag = "URL", start = offset, end = offset)
            .firstOrNull()?.let {
                uriHandler.openUri(it.item)
            }
    }
}