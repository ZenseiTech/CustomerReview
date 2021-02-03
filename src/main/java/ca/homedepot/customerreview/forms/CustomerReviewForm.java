package ca.homedepot.customerreview.forms;

import ca.homedepot.customerreview.util.CourseWords;

import java.io.IOException;
import java.io.Serializable;


/**
 * @author Weichen Zhou
 */
public class CustomerReviewForm implements Serializable
{
	private Double rating;
	private String headline;
	private String comment;

	public Double getRating()
	{
		return rating;
	}

	public void setRating(Double rating)
	{
		this.rating = rating;
	}

	public String getHeadline()
	{
		return headline;
	}

	public void setHeadline(String headline)
	{
		this.headline = headline;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public boolean isRatingNegative() {
		return this.rating < 0;
	}

	public boolean hasCurseWord() {
		return CourseWords.isACourseWord(this.headline) || CourseWords.isACourseWord(this.comment);
	}
}
